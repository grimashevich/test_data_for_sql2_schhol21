import models.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTests {
    @Test
    void isCheckedByVerter() {
        Task task = new Task("C1_Pool", "C1_sample", 100);
        assertTrue(task.isCheckedByVerter());
        task.setTitle("C2_SimpleBashUtils");
        assertTrue(task.isCheckedByVerter());
        task.setTitle("CPP1_s21_matrix+");
        assertFalse(task.isCheckedByVerter());
        task.setTitle("A4_Crypto");
        assertFalse(task.isCheckedByVerter());

    }
}
