package gitlet.commands;

import gitlet.GitletException;
import gitlet.Errors;

public class CommandFactory {
    public static Command parse(String[] args) throws GitletException {
        if (args.length == 0) {
            throw new GitletException(Errors.ERR_NO_COMMAND.getText());
        }
        String firstArg = args[0];
        return switch (firstArg) {
            case "init" -> handleInit();
            case "add" -> handleAdd(args);
            case "commit" -> handleCommit(args);
            case "rm" -> handleRemove(args);
            case "log" -> handleLog();
            case "status" -> handleStatus();
            case "checkout" -> handleCheckout(args);
            case "branch" -> handleBranch(args);
            case "rm-branch" -> handleRemoveBranch(args);
            case "global-log" -> handleGlobalLog();
            case "find" -> handleFind(args);
            case "reset" -> handleReset(args);
            case "merge" -> handleMerge(args);
            case "add-remote" -> handleAddRemote(args);
            case "rm-remote" -> handleRemoveRemote(args);
            case "push" -> handlePush(args);
            case "fetch" -> handleFetch(args);
            case "pull" -> handlePull(args);
            default -> throw new GitletException(Errors.ERR_NO_SUCH_COMMAND.getText());
        };
    }

    private static Command handlePull(String[] args) {
        // Usage: java gitlet.Main pull [remote name] [remote branch name]
        String remoteName = args[1];
        String remoteBranchName = args[2];
        return new PullCommand(remoteName, remoteBranchName);
    }

    private static Command handleFetch(String[] args) {
        // Usage: java gitlet.Main fetch [remote name] [remote branch name]
        String remoteName = args[1];
        String remoteBranchName = args[2];
        return new FetchCommand(remoteName, remoteBranchName);
    }

    private static Command handlePush(String[] args) {
        // Usage: java gitlet.Main push [remote name] [remote branch name]
        String remoteName = args[1];
        String remoteBranchName = args[2];
        return new PushCommand(remoteName, remoteBranchName);
    }

    private static Command handleRemoveRemote(String[] args) {
        // Usage: java gitlet.Main rm-remote [remote name]
        String remoteName = args[1];
        return new RemoveRemoteCommand(remoteName);
    }

    private static Command handleAddRemote(String[] args) {
        // Usage: java gitlet.Main add-remote [remote name] [name of remote directory]/.gitlet
        String remoteName = args[1];
        String remoteDirName = args[2];
        return new AddRemoteCommad(remoteName, remoteDirName);
    }

    private static Command handleMerge(String[] args) {
        // Usage: java gitlet.Main merge [branch name]
        String branchName = args[1];
        return new MergeCommand(branchName);
    }

    private static Command handleReset(String[] args) {
        // Usage: java gitlet.Main reset [commit id]
        String commitID = args[1];
        return new ResetCommand(commitID);
    }

    private static Command handleGlobalLog() {
        // Usage: java gitlet.Main global-log
        return new GlobalLogCommand();
    }

    private static Command handleFind(String[] args) {
        // Usage: java gitlet.Main find [commit message]
        String message = args[1];
        return new FindCommand(message);
    }

    private static Command handleRemoveBranch(String[] args) {
        // Usage: java gitlet.Main rm-branch [branch name]
        String branchName = args[1];
        return new RemoveBranchCommand(branchName);
    }

    private static Command handleBranch(String[] args) {
        // Usage: java gitlet.Main branch [branch name]
        String branchName = args[1];
        return new BranchCommand(branchName);
    }

    private static Command handleCheckout(String[] args) {
        if (args.length == 3) {
            // Usage: java gitlet.Main checkout -- [file name]
            if (!args[1].equals("--")) {
                throw new GitletException("Wrong checkout format!");
            }
            String fileName = args[2];
            return new CheckoutFileCommand(fileName);
        }
        if (args.length == 4) {
            // Usage: java gitlet.Main checkout [commit id] -- [file name]
            String commitId = args[1];
            if (!args[2].equals("--")) {
                throw new GitletException("Incorrect operands.");
            }
            String fileName = args[3];
            return new CheckoutFileCommand(fileName, commitId);
        }
        // Usage: java gitlet.Main checkout [branch name]
        String branchName = args[1];
        return new CheckoutBranchCommand(branchName);
    }

    private static Command handleStatus() {
        // Usage: java gitlet.Main status
        return new StatusCommand();
    }

    private static Command handleLog() {
        // Usage: java gitlet.Main log
        return new LogCommand();
    }

    private static Command handleRemove(String[] args) {
        // Usage: java gitlet.Main rm [file name]
        String fileName = args[1];
        return new RemoveCommand(fileName);
    }

    private static Command handleCommit(String[] args) {
        // Usage: java gitlet.Main commit [message]
        String message = args[1];
        return new CommitCommand(message);
    }

    private static Command handleAdd(String[] args) {
        // Usage: java gitlet.Main add [file name]
        String fileName = args[1];
        return new AddCommand(fileName);
    }

    private static InitCommand handleInit() {
        return new InitCommand();
    }
}
