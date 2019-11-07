package com.cegeka.academy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "challenge_answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChallengeAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 150)
    @Column(name = "video_at", length = 150)
    private String videoAt;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Size(max = 150)
    @Column(name = "answer", length = 150)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChallengeAnswer)) return false;
        ChallengeAnswer that = (ChallengeAnswer) o;
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

    @Override
    public String toString() {
        return "ChallengeAnswer{" +
                "id=" + id +
                ", videoAt='" + videoAt + '\'' +
                ", image=" + Arrays.toString(image) +
                ", answer='" + answer + '\'' +
                '}';
    }
}
