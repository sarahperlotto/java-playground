package lombok;

import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Child extends Parent {
    private boolean vaccinated;
}
