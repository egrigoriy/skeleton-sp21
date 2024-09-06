package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class CheckoutCommand implements Command {
    private final String[] args;
    public CheckoutCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Statuses execute() {
        String fileName;
        if (args.length == 3) {
            fileName = args[2];
            return Repository.checkoutFileFromActiveCommit(fileName);
        }
        // java gitlet.Main checkout [commit id] -- [file name]
        if (args.length == 4) {
            String commitID = args[1];
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            fileName = args[3];
            return Repository.checkoutFileFromCommit(fileName, commitID);
        }
        // java gitlet.Main checkout [branch name]
        if (args.length == 2) {
            String branchName = args[1];
            return Repository.checkoutFilesFromBranchHead(branchName);
        }
        return Statuses.SUCCESS;
    }
}
