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
        try {
            Command command = CommandFactory.parse(args);
            command.execute();
        } catch (GitletException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
