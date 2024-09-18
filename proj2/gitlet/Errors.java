package gitlet;

/**
 * Represents an enumeration of all errors messages
 *
 *  @author Grigoriy Emiliyanov
 */
public enum Errors {
    ERR_NO_COMMAND("Please enter a command."),
    ERR_NO_SUCH_COMMAND("No command with that name exists."),
    ERR_REPO_ALREADY_INIT("A Gitlet version-control system already "
            + "exists in the current directory."),
    ERR_REPO_NOT_INIT("Not in an initialized Gitlet directory."),
    ERR_ADD_FILE_NOT_EXISTS("File does not exist."),
    ERR_REMOVE_FILE_NO_REASON("No reason to remove the file."),
    ERR_COMMIT_EMPTY_MESSAGE("Please enter a commit message."),
    ERR_COMMIT_NOT_FOUND("Found no commit with that message."),
    ERR_COMMIT_NO_CHANGES("No changes added to the commit."),
    ERR_COMMIT_NO_SUCH_EXISTS("No commit with that id exists."),
    ERR_COMMIT_FILE_NOT_EXIST("File does not exist in that commit."),
    ERR_BRANCH_NOT_NEED_CHECKOUT("No need to checkout the current branch."),
    ERR_UNTRACKED_FILES("There is an untracked file in the way; delete it, "
            + "or add and commit it first."),
    ERR_BRANCH_ALREADY_EXIST("A branch with that name already exists."),
    ERR_BRANCH_NOT_EXIST("No such branch exists."),
    ERR_BRANCH_NOT_EXIST2("A branch with that name does not exist."),
    ERR_BRANCH_CANNOT_BE_REMOVED("Cannot remove the current branch."),
    ERR_BRANCH_ANCESTOR("Given branch is an ancestor of the current branch."),
    ERR_BRANCH_FAST_FORWARDED("Current branch fast-forwarded."),
    ERR_BRANCH_CANNOT_MERGE_ITSELF("Cannot merge a branch with itself."),
    ERR_MERGE_UNCOMMITTED_CHANGES("You have uncommitted changes."),

    ERR_REMOTE_ALREADY_EXIST("A remote with that name already exists."),
    ERR_REMOTE_NOT_EXIST("A remote with that name does not exist."),
    ERR_REMOTE_DIR_NOT_FOUND("Remote directory not found."),
    ERR_REMOTE_NO_SUCH_BRANCH("That remote does not have that branch."),
    ERR_LOCAL_BEHIND_REMOTE("Please pull down remote changes before pushing.");
    private String text;
    Errors(String txt) {
        this.text = txt;
    }

    public String getText() {
        return text;
    }
}
