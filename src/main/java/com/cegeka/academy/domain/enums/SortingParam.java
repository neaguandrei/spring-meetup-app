package com.cegeka.academy.domain.enums;

import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.web.rest.errors.InvalidSortingParamException;

import java.util.Arrays;
import java.util.List;

public enum SortingParam {
    NAME{
        @Override
        public void sort(List<UserChallenge> userChallengeList) {
            userChallengeList.sort((o1, o2) -> o1.getUser().getLastName().compareToIgnoreCase(o2.getUser().getLastName()));
        }
    }

    , POINTS{
        @Override
        public void sort(List<UserChallenge> userChallengeList) {
            userChallengeList.sort((o1, o2) -> -Double.compare(o1.getPoints(), o2.getPoints()));
        }
    };

    abstract public void sort(List<UserChallenge> userChallengeList);

    public static SortingParam getSortingParam(String sortingParam)
    {
       return Arrays.stream(values()).filter(enumValue -> sortingParam.equalsIgnoreCase(enumValue.toString()))
                .findFirst()
                .orElseThrow(() -> new InvalidSortingParamException().setMessage("Invalid sorting param"));
    }
}
