package erquar.com.countdown.model;

/**
 * Created by Administrator on 7/23/2016.
 */
public class HolodayModel {
    public String name;
    public String startDate;
    public String endDate;
    public String strDes;

    public HolodayModel() {
        this.name = "";
        this.startDate = "";
        this.endDate = "";
        this.strDes = "";
    }

    public HolodayModel(String name, String startDate, String endDate, String strDes)  {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.strDes = strDes;
    }
}
