package com.clone.twitter.user_service.dto.goal;

import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.goal.GoalStatus;
import com.clone.twitter.user_service.validator.enumvalidator.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalFilterDto {
    private User mentor;

    @Length(max = 64, message = "Title cannot be longer than 64 characters")
    private String title;

    @EnumValidator(enumClass = GoalStatus.class, message = "Invalid Goal Status")
    private GoalStatus goalStatus;
}
