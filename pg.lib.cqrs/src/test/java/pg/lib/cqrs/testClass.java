package pg.lib.cqrs;

public class testClass {

    public static void main(String[] args) {
        UpdateUserCommand command = new UpdateUserCommand();
        UpdateUserCommandHandler ch = new UpdateUserCommandHandler();
        System.out.println(command.getClass());
    }
}
