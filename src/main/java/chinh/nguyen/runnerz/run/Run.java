package chinh.nguyen.runnerz.run;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Run(
                  @Id
                  Integer id,
                  @NotEmpty
                  String title,
                  LocalDateTime startedOn,
                  LocalDateTime completedOn,
                  @Positive
                  Integer miles,
                  Location location,
                  @Version
                  Integer version
) {

    public Run {
        //Think of constructure like a validation tool, because It will check everytime get initialized
        if (!completedOn.isAfter(startedOn)) {
            throw new IllegalArgumentException("Completed on must be after started on");
        }

    }
}
