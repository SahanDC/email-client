package assignment;

public class Personal extends Recipient implements IGreet{
    private String nickName;
    private Date birthday = new Date();

    public Personal(String name, String nickName, String email, Date birthday) {
        super(name, email);
        this.nickName = nickName;
        this.birthday = birthday;
    }

    public String getNickName() {
        return nickName;
    }

    public Date getBirthday() {
        return birthday;
    }
}
