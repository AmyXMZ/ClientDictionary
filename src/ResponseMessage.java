import java.util.List;

public class ResponseMessage {
    public String errorMessage;
    public String status;
    public List<String> meanings;

    //getters
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getMeanings() {
        return meanings;
    }


}
