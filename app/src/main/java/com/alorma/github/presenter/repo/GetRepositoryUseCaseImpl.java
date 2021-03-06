package com.alorma.github.presenter.repo;

import com.alorma.github.sdk.bean.info.CommitInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import core.datasource.SdkItem;
import core.repositories.Branch;
import core.repositories.Commit;
import core.repositories.GitCommit;
import core.repositories.Repo;
import core.repository.CheckStarRepository;
import core.repository.GenericRepository;
import core.repository.GetRepositoryWatchRepository;
import java.util.List;
import rx.Observable;

public class GetRepositoryUseCaseImpl implements GetRepositoryUseCase {
  private final GenericRepository<RepoInfo, Repo> repoGenericRepository;
  private final GenericRepository<RepoInfo, List<Branch>> branchesGenericRepository;
  private final GenericRepository<CommitInfo, Commit> commitGenericRepository;
  private final CheckStarRepository repoStarredRepository;
  private GetRepositoryWatchRepository repoWatchedRepository;

  public GetRepositoryUseCaseImpl(GenericRepository<RepoInfo, Repo> repoGenericRepository,
      GenericRepository<RepoInfo, List<Branch>> branchesGenericRepository,
      GenericRepository<CommitInfo, Commit> commitGenericRepository,
      CheckStarRepository repoStarredRepository,
      GetRepositoryWatchRepository getWatchUseCase) {

    this.repoGenericRepository = repoGenericRepository;
    this.branchesGenericRepository = branchesGenericRepository;
    this.commitGenericRepository = commitGenericRepository;
    this.repoStarredRepository = repoStarredRepository;
    this.repoWatchedRepository = getWatchUseCase;
  }

  @Override
  public Observable<SdkItem<Repo>> getRepository(RepoInfo repoInfo) {
    return getRepository(repoInfo, false);
  }

  @Override
  public Observable<SdkItem<Repo>> getRepository(RepoInfo repoInfo, boolean refresh) {
    SdkItem<RepoInfo> repoInfoSdkItem = new SdkItem<>(repoInfo);
    Observable<Repo> repoObservable = repoGenericRepository.execute(repoInfoSdkItem, refresh).map(SdkItem::getK);

    Observable<Boolean> starredObservable = repoStarredRepository.check(repoInfo);
    Observable<Boolean> watchedObservable = repoWatchedRepository.check(repoInfo);

    Observable<Repo> observable = Observable.zip(repoObservable, starredObservable, watchedObservable, (repo, starred, watched) -> {
      repo.setStarred(starred);
      repo.setWatched(watched);
      return repo;
    });
    Observable<List<Branch>> branchObservable =
        branchesGenericRepository.execute(repoInfoSdkItem).map(SdkItem::getK).flatMap(Observable::from).flatMap((branch) -> {
          CommitInfo info = new CommitInfo();
          info.repoInfo = repoInfo;
          info.sha = branch.commit.sha;
          return commitGenericRepository.execute(new SdkItem<>(info)).map(SdkItem::getK);
        }, (branch, commit) -> {
          if (branch.commit.sha.equals(commit.sha)) {
            branch.commit = new Commit();
            branch.commit.sha = commit.sha;
            branch.commit.author = commit.author;
            if (commit.commit != null) {
              branch.commit.commit = new GitCommit();
              branch.commit.commit.author = commit.commit.author;
              branch.commit.commit.sha = commit.commit.sha;
            }
          }
          return branch;
        }).toList();

    observable = Observable.zip(observable, branchObservable, (repo, branches) -> {
      repo.setBranches(branches);
      if (branches != null && branches.size() > 0) {
        if (repo.defaultBranch == null) {
          repo.defaultBranch = branches.get(0).name;
          repo.defaultBranchObject = branches.get(0);
        } else {
          for (Branch branch : branches) {
            if (repo.defaultBranch.equals(branch.name)) {
              repo.defaultBranchObject = branch;
            }
          }
        }
      }

      return repo;
    });

    observable = observable.doOnNext(repo -> repoGenericRepository.save(repoInfo, repo));
    return observable.map(SdkItem::new);
  }
}
