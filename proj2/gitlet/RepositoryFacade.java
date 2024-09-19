package gitlet;

/** Represents a Facade to Repository.
 *
 *  @author Grigoriy Emiliyanov
 */
public class RepositoryFacade {

    /**
     * Initializes the repository.
     * Throws exception in case of error.
     * @throws GitletException
     */
    public static void init() throws GitletException {
        if (Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_ALREADY_INIT.getText());
        }
        Repository.init();
    }

    /**
     * Add file with given file name to the Repository
     * Throws exception in case of error.
     * @param fileName
     * @throws GitletException
     */
    public static void addFile(String fileName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!WorkingDir.fileExists(fileName)) {
            throw new GitletException(Errors.ERR_ADD_FILE_NOT_EXISTS.getText());
        }
        Index index = Repository.readIndex();
        index.add(fileName);
        Repository.saveIndex(index);
    }

    /**
     * Removes file with given file name from the index.
     * Throws exception in case of error.
     * @param fileName
     * @throws GitletException
     */
    public static void removeFile(String fileName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Repository.readIndex();
        if (index.isUntracked(fileName)) {
            throw new GitletException(Errors.ERR_REMOVE_FILE_NO_REASON.getText());
        }
        index.remove(fileName);
        Repository.saveIndex(index);
    }

    /**
     *  Prints the status of the index including branches information.
     * Throws exception in case of error.
     * @throws GitletException
     */
    public static void status() throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Repository.readIndex();
        System.out.println(index.status());
    }

    /**
     *  Prints the history of the active branch.
     * Throws exception in case of error.
     * @throws GitletException
     */
    public static void log() throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        System.out.println(Repository.log());
    }

    /**
     *  Prints all commits.
     * Throws exception in case of error.
     * @throws GitletException
     */
    public static void globalLog() throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        System.out.println(Repository.allCommitsAsString());
    }

    /**
     * Makes a commit of staged files and clear the index.
     * Throws exception in case of error.
     * @param message
     * @throws GitletException
     */
    public static void commit(String message) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (message.isEmpty()) {
            throw new GitletException(Errors.ERR_COMMIT_EMPTY_MESSAGE.getText());
        }
        Index index = Repository.readIndex();
        if (index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_COMMIT_NO_CHANGES.getText());
        }
        Repository.makeCommit(message, index);
        index.clear();
        Repository.saveIndex(index);
    }

    /**
     * Checkouts file with given name from the active commit.
     * Throws exception in case of error.
     * @param fileName
     * @throws GitletException
     */
    public static void checkoutFileFromActiveCommit(String fileName) throws GitletException {
        Repository.checkoutFileFromActiveCommit(fileName);
    }

    /**
     * Checkouts file with given name from a commit with given id.
     * Throws exception in case of error.
     * @param fileName
     * @param commitId
     * @throws GitletException
     */
    public static void checkoutFileFromCommit(String fileName, String commitId)
            throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Repository.getCommit(commitId);
        if (commit == null) {
            throw new GitletException(Errors.ERR_COMMIT_NO_SUCH_EXISTS.getText());
        }
        if (!commit.hasFile(fileName)) {
            throw new GitletException(Errors.ERR_COMMIT_FILE_NOT_EXIST.getText());
        }
        Repository.checkoutFileFromCommit(fileName, commit);
    }

    /**
     * Checkouts all files from the head commit of a branch with given name.
     * Index is cleared afterward. Branch with the given name is activated.
     * Throws exception in case of error.
     * @param branchName
     * @throws GitletException
     */
    public static void checkoutFilesFromBranchHead(String branchName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch branch = new Branch(branchName);
        if (!branch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST.getText());
        }
        if (branch.isActive()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_NEED_CHECKOUT.getText());
        }

        Index index = Repository.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Commit branchHeadCommit = branch.getHeadCommit();
        Repository.checkoutFilesFromCommit(branchHeadCommit);
        index.clear();
        index.setRepo(branchHeadCommit.getFilesTable());
        Repository.saveIndex(index);
        branch.activate();
    }

    /**
     * Checkouts all files tracked by a commit with given id and set it as current branch head
     * Index is cleared afterward. The commit with given id is set as head of the current branch.
     * Throws exception in case of error.
     * @param commitId
     * @throws GitletException
     */
    public static void reset(String commitId) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Repository.getCommit(commitId);
        if (commit == null) {
            throw new GitletException(Errors.ERR_COMMIT_NO_SUCH_EXISTS.getText());
        }
        Index index = Repository.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Repository.checkoutFilesFromCommit(commit);
        index.clear();
        index.setRepo(commit.getFilesTable());
        Repository.saveIndex(index);
        Repository.setActiveCommitTo(commitId);
    }

    /**
     * Creates a branch with given name.
     * Throws exception in case of error.
     * @param branchName
     * @throws GitletException
     */
    public static void createBranch(String branchName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch branch = new Branch(branchName);
        if (branch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_ALREADY_EXIST.getText());
        }
        branch.create();
    }

    /**
     * Removes branch with given name.
     * Throws exception in case of error.
     * @param branchName
     * @throws GitletException
     */
    public static void removeBranch(String branchName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch branch = new Branch(branchName);
        if (!branch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (branch.isActive()) {
            throw new GitletException(Errors.ERR_BRANCH_CANNOT_BE_REMOVED.getText());
        }
        branch.remove();
    }

    /**
     * Prints all commits with message containing given search text.
     * Throws exception in case of error.
     * @param searchText
     * @throws GitletException
     */
    public static void find(String searchText) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        String foundCommits = Repository.find(searchText);
        if (foundCommits.isEmpty()) {
            throw new GitletException(Errors.ERR_COMMIT_NOT_FOUND.getText());
        }
        System.out.println(foundCommits);
    }

    /**
     * Merges the branch with given name to the active branch.
     * Index is updated with result of merge and then commit is made.
     * At the end index is cleared.
     * Throws exception in case of error.
     * @param branchName
     * @throws GitletException
     */
    public static void merge(String branchName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch otherBranch = new Branch(branchName);
        if (!otherBranch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (otherBranch.isActive()) {
            throw new GitletException(Errors.ERR_BRANCH_CANNOT_MERGE_ITSELF.getText());
        }
        Index index = Repository.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        if (!index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_MERGE_UNCOMMITTED_CHANGES.getText());
        }
        Branch currentBranch = new Branch();
        Commit activeCommit = currentBranch.getHeadCommit();
        Commit otherCommit = otherBranch.getHeadCommit();
        Commit splitCommit = Repository.findSplitCommit(activeCommit, otherCommit);
        if (otherCommit.equals(splitCommit)) {
            throw new GitletException(Errors.ERR_BRANCH_ANCESTOR.getText());
        }
        if (activeCommit.equals(splitCommit)) {
            checkoutFilesFromBranchHead(branchName);
            throw new GitletException(Errors.ERR_BRANCH_FAST_FORWARDED.getText());
        }
        index.updateOnMerge(index, activeCommit, otherCommit, splitCommit);
        String message = "Merged " + branchName + " into " + currentBranch.getName() + ".";
        Repository.makeCommit(message, index, otherCommit.getUid());
        index.clear();
        Repository.saveIndex(index);
    }

    /**
     * Adds under given name a remote for tracking which is located at given directory.
     * Throws exception in case of error.
     * @param remoteName
     * @param remoteDirName
     * @throws GitletException
     */
    public static void addRemote(String remoteName, String remoteDirName) throws GitletException {
        Remote remote = new Remote(remoteName);
        if (remote.exists()) {
            throw new GitletException(Errors.ERR_REMOTE_ALREADY_EXIST.getText());
        }
        remote.add(remoteDirName);
    }

    /**
     * Removes tracking of remote having given name.
     * Throws exception in case of error.
     * @param remoteName
     * @throws GitletException
     */
    public static void removeRemote(String remoteName) throws GitletException {
        Remote remote = new Remote(remoteName);
        if (!remote.exists()) {
            throw new GitletException(Errors.ERR_REMOTE_NOT_EXIST.getText());
        }
        remote.remove();
    }

    /**
     * Pushes branch with given name to a remote tracked under given name.
     * Throws exception in case of error.
     * @param remoteName
     * @param remoteBranchName
     * @throws GitletException
     */
    public static void push(String remoteName, String remoteBranchName) throws GitletException {
        Remote remote = new Remote(remoteName);
        if (!remote.remoteUrlExists()) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!remote.branchExists(remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        if (remote.isLocalBehindRemote(remoteBranchName)) {
            throw new GitletException(Errors.ERR_LOCAL_BEHIND_REMOTE.getText());
        }
        remote.push(remoteBranchName);
    }

    /**
     * Fetches the branch with given name from remote tracked under given name.
     * Throws exception in case of error.
     * @param remoteName
     * @param remoteBranchName
     * @throws GitletException
     */
    public static void fetch(String remoteName, String remoteBranchName) throws GitletException {
        Remote remote = new Remote(remoteName);
        if (!remote.remoteUrlExists()) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!remote.branchExists(remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        remote.fetch(remoteBranchName);
    }

    /**
     * Pulls the branch with given name from remote tracked under given name.
     * First fetch remote branch then merge with it
     * Throws exception in case of error.
     * @param remoteName
     * @param remoteBranchName
     * @throws GitletException
     */
    public static void pull(String remoteName, String remoteBranchName) throws GitletException {
        fetch(remoteName, remoteBranchName);
        String branchName = remoteName + "/" + remoteBranchName;
        merge(branchName);
    }
}
