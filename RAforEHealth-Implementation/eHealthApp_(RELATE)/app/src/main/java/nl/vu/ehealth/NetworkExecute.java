package nl.vu.ehealth;
import android.util.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.List;

//Execute: Send the data to the backend or connect to the available network
public class NetworkExecute {
    //For the logs used to debug
    private static final String TAG = "DEBUG:ICM-E";
    //Transfer the data to Mongodb backend
    public void transferData(List<User> userList){
         //Debug message
        Log.v(TAG, "Send the data to the mongodb backend");
        //Send the data to backend mongodb
        try {
            //Had to set the localhost to 10.0.2.2 so that I could connect the emulator to the pc localhost
            MongoClientURI uri  = new MongoClientURI("mongodb://10.0.2.2:27017/test");
            MongoClient client = new MongoClient(uri);

            MongoDatabase db = client.getDatabase(uri.getDatabase());
            MongoCollection<BasicDBObject> collection = db.getCollection("sendtest", BasicDBObject.class);
            //Get the data for each user object and put it in the mongodb document
            for (User user:userList) {
                BasicDBObject document = new BasicDBObject();
                document.put("name", user.firstName);
                document.put("lastname", user.lastName);
                collection.insertOne(document);
            }
            MongoCursor iterator = collection.find().iterator();
            //Print the mongodb data inserted. THIS IS JUST A CHECK
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }//The connection to the backend mongodb failed
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    //Connect to a new network
    public void connectToNewNetwork(){
        //Debug message
        Log.v(TAG, "Connected to a different network");

    }
}
