package gitlet.commands;

import gitlet.GitletException;
import gitlet.Errors;

public class CommandFactory {
    public static Command parse(String[] args) throws GitletException {
        if (args.length == 0) {
            throw new GitletException(Errors.ERR_NO_COMMAND.getText());
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                return new InitCommand();
            case "add": // Usage: java gitlet.Main add [file name]
                String fileName = args[1];
                return new AddCommand(fileName);
            case "commit": // Usage: java gitlet.Main commit [message]
                String message = args[1];
                return new CommitCommand(message);
            case "rm": // Usage: java gitlet.Main rm [file name]
                fileName = args[1];
                return new RemoveCommand(fileName);
            case "log": // Usage: java gitlet.Main log
                return new LogCommand();
            case "status": // Usage: java gitlet.Main status
                return new StatusCommand();
            case "checkout":
                if (args.length == 2) {
                    String branchName = args[1];
                    return new CheckoutBranchCommand(branchName);
                }
                if (args.length == 3) { // Usage: java gitlet.Main checkout -- [file name]
                    if (!args[1].equals("--")) {
                        throw new GitletException("Wrong checkout format!");
                    }
                    fileName = args[2];
                    return new CheckoutFileCommand(fileName);
                }
                if (args.length == 4) {
                    String commitId = args[1];
                    if (!args[2].equals("--")) {
                        throw new GitletException("Incorrect operands.");
                    }
                    fileName = args[3];
                    return new CheckoutFileCommand(fileName, commitId);
                }
                break;
            case "branch":
                String branchName = args[1];
                return new BranchCommand(branchName);
            case "rm-branch":
                branchName = args[1];
                return new RemoveBranchCommand(branchName);
            case "global-log":
                return new GlobalLogCommand();
            case "find":
                message = args[1];
                return new FindCommand(message);
            case "reset":
                String commitID = args[1];
                return new ResetCommand(commitID);
            case "merge":
                branchName = args[1];
                return new MergeCommand(branchName);
            case "add-remote":
                String remoteName = args[1];
                String remoteDirName = args[2];
                return new AddRemoteCommad(remoteName, remoteDirName);
            case "rm-remote":
                remoteName = args[1];
                return new RemoveRemoteCommand(remoteName);
            case "push":
                remoteName = args[1];
                String remoteBranchName = args[2];
                return new PushCommand(remoteName, remoteBranchName);
            case "fetch":
                remoteName = args[1];
                remoteBranchName = args[2];
                return new FetchCommand(remoteName, remoteBranchName);
            case "pull":
                remoteName = args[1];
                remoteBranchName = args[2];
                return new PullCommand(remoteName, remoteBranchName);
        }
        throw new GitletException(Errors.ERR_NO_SUCH_COMMAND.getText());
    }
}
