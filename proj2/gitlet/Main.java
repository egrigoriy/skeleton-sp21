package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
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
                repository = new Repository();
                repository.init();
                break;
            case "status":
                repository = new Repository();
                repository.status();
            case "add":
                // TODO: handle the `add [filename]` command
                repository = new Repository();
                String fileName = args[1];
                repository.add(fileName);
                break;
            case "log":
                repository = new Repository();
                repository.log();
                break;
            case "commit":
                repository = new Repository();
                String message = args[1];
                repository.commit(message);
                break;
            case "checkout":
                // java gitlet.Main checkout -- [file name]
                // java gitlet.Main checkout [commit id] -- [file name]
                // java gitlet.Main checkout [branch name]
                repository = new Repository();
                if (args.length == 3) {
                    fileName = args[2];
                    repository.checkoutFileFromLastCommit(fileName);
                }
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
