package dding.profile.execption;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String userId) {
        super("해당 사용자의 프로필이 존재하지 않습니다. userId: " + userId);
    }
}
