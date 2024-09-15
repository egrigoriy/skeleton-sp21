package gitlet.commands;

import gitlet.GitletException;
import gitlet.Errors;

/**
 *  Factory providing the command corresponding to the given args
 *  @author Grigoriy Emiliyanov
 */

public class CommandFactory {

    /**
     * Returns the command corresponding to the given args.
     * In case of error, throws a GitletExeption
     *
     * @param args
     * @return a Command
     * @throws GitletException
     */
    public static Command parse(String[] args) throws GitletException {
        if (args.length == 0) {
            throw new GitletException(Errors.ERR_NO_COMMAND.getText());
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                return handleInit();
            case "add":
                return handleAdd(args);
            case "commit":
                return handleCommit(args);
            case "rm":
                return handleRemove(args);
            case "log":
                return handleLog();
            case "status":
                return handleStatus();
            case "checkout":
                return handleCheckout(args);
            case "branch":
                return handleBranch(args);
            case "rm-branch":
                return handleRemoveBranch(args);
            case "global-log":
                return handleGlobalLog();
            case "find":
                return handleFind(args);
            case "reset":
                return handleReset(args);
            case "merge":
                return handleMerge(args);
            case "add-remote":
                return handleAddRemote(args);
            case "rm-remote":
                return handleRemoveRemote(args);
            case "push":
                return handlePush(args);
            case "fetch":
                return handleFetch(args);
            case "pull":
                return handlePull(args);
            default:
                throw new GitletException(Errors.ERR_NO_SUCH_COMMAND.getText());
        }
    }

    /**
     * Parses args and returns an InitCommand
     * Usage: java gitlet.Main init
     *
     * @return new InitCommand
     */
    private static InitCommand handleInit() {
        return new InitCommand();
    }

    /**
     * Parses args and returns an AddCommand
     * Usage: java gitlet.Main add [file name]
     *
     * @return new AddCommand
     */
    private static Command handleAdd(String[] args) {
        validateNumArgs("add", args, 2);
        String fileName = args[1];
        return new AddCommand(fileName);
    }

    /**
     * Parses args and returns a CommitCommand
     * Usage: java gitlet.Main commit [message]
     *
     * @return new CommitCommand
     */
    private static Command handleCommit(String[] args) {
        validateNumArgs("commit", args, 2);
        String message = args[1];
        return new CommitCommand(message);
    }

    /**
     * Parses args and returns a RemoveFileCommand
     * Usage: java gitlet.Main rm [file name]
     *
     * @return new RemoveFileCommand
     */
    private static Command handleRemove(String[] args) {
        validateNumArgs("remove", args, 2);
        String fileName = args[1];
        return new RemoveFileCommand(fileName);
    }

    /**
     * Parses args and returns a LogCommand
     * Usage: java gitlet.Main log
     *
     * @return new LogCommand
     */
    private static Command handleLog() {
        return new LogCommand();
    }

    /**
     * Parses args and returns a StatusCommand
     * Usage: java gitlet.Main status
     *
     * @return new StatusCommand
     */
    private static Command handleStatus() {
        return new StatusCommand();
    }

    /**
     * Parses args and returns a corresponding checkout command
     * Usage: java gitlet.Main checkout -- [file name]
     * Usage: java gitlet.Main checkout [commit id] -- [file name]
     * Usage: java gitlet.Main checkout [branch name]
     *
     * @return a corresponding checkout command
     */
    private static Command handleCheckout(String[] args) {
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                throw new GitletException("Wrong checkout format!");
            }
            String fileName = args[2];
            return new CheckoutFileCommand(fileName);
        }
        if (args.length == 4) {
            String commitId = args[1];
            if (!args[2].equals("--")) {
                throw new GitletException("Incorrect operands.");
            }
            String fileName = args[3];
            return new CheckoutFileCommand(fileName, commitId);
        }
        String branchName = args[1];
        return new CheckoutBranchCommand(branchName);
    }

    /**
     * Parses args and returns a BranchCommand
     * Usage: java gitlet.Main branch [branch name]
     *
     * @return new BranchCommand
     */
    private static Command handleBranch(String[] args) {
        validateNumArgs("branch", args, 2);
        String branchName = args[1];
        return new BranchCommand(branchName);
    }

    /**
     * Parses args and returns a RemoveBranchCommand
     * Usage: java gitlet.Main rm-branch [branch name]
     *
     * @return new RemoveBranchCommand
     */
    private static Command handleRemoveBranch(String[] args) {
        validateNumArgs("rm-branch", args, 2);
        String branchName = args[1];
        return new RemoveBranchCommand(branchName);
    }

    /**
     * Parses args and returns a GlobalLogComamnd
     * Usage: java gitlet.Main global-log
     *
     * @return new GlobalLogComamnd
     */
    private static Command handleGlobalLog() {
        return new GlobalLogCommand();
    }

    /**
     * Parses args and returns a FindCommand
     * Usage: java gitlet.Main find [commit message]
     *
     * @return new FindCommand
     */
    private static Command handleFind(String[] args) {
        validateNumArgs("find", args, 2);
        String message = args[1];
        return new FindCommand(message);
    }

    /**
     * Parses args and returns a ResetCommand
     * Usage: java gitlet.Main reset [commit id]
     *
     * @return new ResetCommand
     */
    private static Command handleReset(String[] args) {
        validateNumArgs("reset", args, 2);
        String commitID = args[1];
        return new ResetCommand(commitID);
    }

    /**
     * Parses args and returns a MergeCommand
     * Usage: java gitlet.Main merge [branch name]
     *
     * @return new MergeCommand
     */
    private static Command handleMerge(String[] args) {
        validateNumArgs("merge", args, 2);
        String branchName = args[1];
        return new MergeCommand(branchName);
    }

    /**
     * Parses args and returns an AddRemoteCommand
     * Usage:java gitlet.Main add-remote [remote name] [name of remote directory]/.gitlet
     *
     * @return new AddRemoteCommand
     */
    private static Command handleAddRemote(String[] args) {
        validateNumArgs("add-remote", args, 3);
        String remoteName = args[1];
        String remoteDirName = args[2];
        return new AddRemoteCommad(remoteName, remoteDirName);
    }

    /**
     * Parses args and returns a RemoveRemoteCommand
     * Usage: java gitlet.Main rm-remote [remote name]
     *
     * @return new RemoveRemoteCommand
     */
    private static Command handleRemoveRemote(String[] args) {
        validateNumArgs("rm-remote", args, 2);
        String remoteName = args[1];
        return new RemoveRemoteCommand(remoteName);
    }

    /**
     * Parses args and returns a PushCommand
     * Usage: java gitlet.Main push [remote name] [remote branch name]
     *
     * @return new PushCommand
     */
    private static Command handlePush(String[] args) {
        validateNumArgs("push", args, 3);
        String remoteName = args[1];
        String remoteBranchName = args[2];
        return new PushCommand(remoteName, remoteBranchName);
    }

    /**
     * Parses args and returns a FetchCommand
     * Usage: java gitlet.Main fetch [remote name] [remote branch name]
     *
     * @return new FetchCommand
     */
    private static Command handleFetch(String[] args) {
        validateNumArgs("fetch", args, 3);
        String remoteName = args[1];
        String remoteBranchName = args[2];
        return new FetchCommand(remoteName, remoteBranchName);
    }

    /**
     * Parses args and returns a PullCommand
     * Usage: java gitlet.Main pull [remote name] [remote branch name]
     *
     * @return new PullCommand
     */
    private static Command handlePull(String[] args) {
        validateNumArgs("pull", args, 3);
        String remoteName = args[1];
        String remoteBranchName = args[2];
        return new PullCommand(remoteName, remoteBranchName);
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
