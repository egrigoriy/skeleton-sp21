package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Grigoriy Emiliyanov
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        Status status = Status.SUCCESS;
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // Usage: java gitlet.Main init
                status = Repository.init();
                break;
            case "add":
                // Usage: java gitlet.Main add [file name]
                String fileName = args[1];
                status = Repository.add(fileName);
                break;
            case "commit":
                // Usage: java gitlet.Main commit [message]
                String message = args[1];
                status = Repository.commit(message);
                break;
            case "rm":
                // Usage: java gitlet.Main rm [file name]
                fileName = args[1];
                status = Repository.remove(fileName);
                break;
            case "log":
                // Usage: java gitlet.Main log
                status = Repository.log();
                break;
            case "status":
                // Usage: java gitlet.Main status
                status = Repository.status();
                break;
            case "checkout":
                // java gitlet.Main checkout -- [file name]
                if (args.length == 3) {
                    fileName = args[2];
                    status = Repository.checkoutFileFromActiveCommit(fileName);
                }
                // java gitlet.Main checkout [commit id] -- [file name]
                if (args.length == 4) {
                    String commitID = args[1];
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    fileName = args[3];
                    status = Repository.checkoutFileFromCommit(fileName, commitID);
                }
                // java gitlet.Main checkout [branch name]
                if (args.length == 2) {
                    String branchName = args[1];
                    status = Repository.checkoutFilesFromBranchHead(branchName);
                }
                break;
            case "branch":
                // Usage: java gitlet.Main branch [branch name]
                String branchName = args[1];
                status = Repository.branch(branchName);
                break;
            case "rm-branch":
                // Usage: java gitlet.Main rm-branch [branch name]
                branchName = args[1];
                status = Repository.removeBranch(branchName);
                break;
            case "global-log":
                // Usage: java gitlet.Main global-log
                status = Repository.globalLog();
                break;
            case "find":
                // java gitlet.Main find [commit message]
                message = args[1];
                status = Repository.find(message);
                break;
            case "reset":
                // Usage: java gitlet.Main reset [commit id]
                String commitID = args[1];
                status = Repository.reset(commitID);
                break;
            case "merge":
                // Usage: java gitlet.Main merge [branch name]
                branchName = args[1];
                status = Repository.merge(branchName);
                break;
            case "add-remote":
                // Usage: java gitlet.Main add-remote [remote name] [name of remote directory]/.gitlet
                String remoteName = args[1];
                String remoteDirName = args[2];
                status = Repository.addRemote(remoteName, remoteDirName);
                break;
            case "rm-remote":
                // Usage: java gitlet.Main rm-remote [remote name]
                remoteName = args[1];
                status = Repository.removeRemote(remoteName);
                break;
            case "push":
                // Usage: java gitlet.Main push [remote name] [remote branch name]
                remoteName = args[1];
                String remoteBranchName = args[2];
                status = Repository.push(remoteName, remoteBranchName);
                break;
            case "fetch":
                // Usage: java gitlet.Main fetch [remote name] [remote branch name]
                remoteName = args[1];
                remoteBranchName = args[2];
                status = Repository.fetch(remoteName, remoteBranchName);
                break;
            case "pull":
                // Usage: java gitlet.Main pull [remote name] [remote branch name]
                remoteName = args[1];
                remoteBranchName = args[2];
                status = Repository.pull(remoteName, remoteBranchName);
                break;
            default:
                status = Status.ERR_NO_SUCH_COMMAND;
        }
        if (status != Status.SUCCESS) {
            System.out.printf(status.text);
            System.exit(0);
        }
    }
}
