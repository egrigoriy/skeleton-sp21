package gitlet.commands;

public class CommandFactory {
    public static Command parse(String[] args) {
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // Usage: java gitlet.Main init
                return new InitCommand();
            case "add":
                // Usage: java gitlet.Main add [file name]
                String fileName = args[1];
                return new AddCommand(fileName);
            case "commit":
                // Usage: java gitlet.Main commit [message]
                String message = args[1];
                return new CommitCommand(message);
            case "rm":
                // Usage: java gitlet.Main rm [file name]
                fileName = args[1];
                return new RemoveCommand(fileName);
            case "log":
                // Usage: java gitlet.Main log
                return new LogCommand();
            case "status":
                // Usage: java gitlet.Main status
                return new StatusCommand();
            case "checkout":
                return new CheckoutCommand(args);
            case "branch":
                // Usage: java gitlet.Main branch [branch name]
                String branchName = args[1];
                return new BranchCommand(branchName);
            case "rm-branch":
                // Usage: java gitlet.Main rm-branch [branch name]
                branchName = args[1];
                return new RemoveBranchCommand(branchName);
            case "global-log":
                // Usage: java gitlet.Main global-log
                return new GlobalLogCommand();
            case "find":
                // java gitlet.Main find [commit message]
                message = args[1];
                return new FindCommand(message);
            case "reset":
                // Usage: java gitlet.Main reset [commit id]
                String commitID = args[1];
                return new ResetCommand(commitID);
            case "merge":
                // Usage: java gitlet.Main merge [branch name]
                branchName = args[1];
                return new MergeCommand(branchName);
//            case "add-remote":
//                // Usage: java gitlet.Main add-remote [remote name] [name of remote directory]/.gitlet
//
//                String remoteName = args[1];
//                String remoteDirName = args[2];
//                status = Repository.addRemote(remoteName, remoteDirName);
//                break;
//            case "rm-remote":
//                // Usage: java gitlet.Main rm-remote [remote name]
//                remoteName = args[1];
//                status = Repository.removeRemote(remoteName);
//                break;
//            case "push":
//                // Usage: java gitlet.Main push [remote name] [remote branch name]
//                remoteName = args[1];
//                String remoteBranchName = args[2];
//                status = Repository.push(remoteName, remoteBranchName);
//                break;
//            case "fetch":
//                // Usage: java gitlet.Main fetch [remote name] [remote branch name]
//                remoteName = args[1];
//                remoteBranchName = args[2];
//                status = Repository.fetch(remoteName, remoteBranchName);
//                break;
//            case "pull":
//                // Usage: java gitlet.Main pull [remote name] [remote branch name]
//                remoteName = args[1];
//                remoteBranchName = args[2];
//                status = Repository.pull(remoteName, remoteBranchName);
//                break;
            default:
                return new NoSuchCommand();
        }
    }
}
