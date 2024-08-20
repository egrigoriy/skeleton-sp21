package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Grigoriy Emiliyanov
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        Repository repository;
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "status":
                Repository.status();
                break;
            case "add":
                //`add [filename]`
                String fileName = args[1];
                Repository.add(fileName);
                break;
            case "rm":
                fileName = args[1];
                Repository.remove(fileName);
                break;
            case "log":
                Repository.log();
                break;
            case "commit":
                String message = args[1];
                Repository.commit(message);
                break;
            case "checkout":
                // java gitlet.Main checkout -- [file name]
                if (args.length == 3) {
                    fileName = args[2];
                    Repository.checkoutFileFromLastCommit(fileName);
                }
                // java gitlet.Main checkout [commit id] -- [file name]
                if (args.length == 4) {
                    String commitID = args[1];
                    fileName = args[3];
                    Repository.checkoutFileFromCommit(fileName, commitID);
                }
                // java gitlet.Main checkout [branch name]
                if (args.length == 2) {
                    String branchName = args[1];
                    Repository.checkoutFilesFromBranchHead(branchName);
                }
                break;
            case "branch":
                // Usage: java gitlet.Main branch [branch name]
                String branchName = args[1];
                Repository.branch(branchName);
                break;
            case "rm-branch":
                // Usage: java gitlet.Main rm-branch [branch name]
                branchName = args[1];
                Repository.removeBranch(branchName);

            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
