package gitlet;

import gitlet.commands.Command;
import gitlet.commands.CommandFactory;

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
        Command command = CommandFactory.parse(args);
        Statuses status = command.execute();
        if (status != Statuses.SUCCESS) {
            System.out.println(status.getText());
            System.exit(0);
        }
    }
}
