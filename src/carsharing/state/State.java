package carsharing.state;

import java.sql.SQLException;

public interface State {
    void execute() throws SQLException;
}
