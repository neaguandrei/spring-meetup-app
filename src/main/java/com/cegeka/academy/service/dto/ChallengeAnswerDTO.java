package com.cegeka.academy.service.dto;


import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

public class ChallengeAnswerDTO {

    private Long id;
    private String videoAt;
    private byte[] image;

    @NotNull(message = "Answer must not be null.")
    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoAt() {
        return videoAt;
    }

    public void setVideoAt(String videoAt) {
        this.videoAt = videoAt;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "ChallengeAnswerDTO{" +
                "id=" + id +
                ", videoAt='" + videoAt + '\'' +
                ", image=" + Arrays.toString(image) +
                ", answer='" + answer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChallengeAnswerDTO)) return false;
        ChallengeAnswerDTO that = (ChallengeAnswerDTO) o;
        return getId().equals(that.getId()) &&
                getVideoAt().equals(that.getVideoAt()) &&
                Arrays.equals(getImage(), that.getImage()) &&
                getAnswer().equals(that.getAnswer());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getVideoAt(), getAnswer());
        result = 31 * result + Arrays.hashCode(getImage());
        return result;
    }
}
