package data;

public class AnimeReview {
    int id;
    int userId;
    String userName;
    String comment;
    String animeName;

    public AnimeReview(int id, int userId, String userName, String comment, String animeName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.animeName = animeName;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getAnimeName() {
        return animeName;
    }

    @Override
    public String toString() {
        return "AnimeReview{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", comment='" + comment + '\'' +
                ", animeName='" + animeName + '\'' +
                '}';
    }
}
