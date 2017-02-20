package io.skygear.skygear_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

import io.skygear.skygear.Container;
import io.skygear.skygear.Database;
import io.skygear.skygear.Error;
import io.skygear.skygear.Query;
import io.skygear.skygear.Record;
import io.skygear.skygear.RecordQueryResponseHandler;
import io.skygear.skygear.RecordSaveResponseHandler;
import io.skygear.skygear.Reference;

public class DangerZoneActivity extends AppCompatActivity {


    private static final String TABLE_NAME_GROUP = "idms_group";
    private static final String TABLE_NAME_USER = "idms_user";
    private static final String TABLE_NAME_GROUPING = "idms_grouping";


    private Container mSkygear;
    private TextView mTextView;
    private Button mButtonAdd;
    private Button mButtonGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zone);

        mTextView = (TextView) findViewById(R.id.textview);

        mSkygear = Container.defaultContainer(this);
    }





    public void getDummyData(View view){
        Database publicDatabase = mSkygear.getPublicDatabase();
        Query groupQuery = new Query(TABLE_NAME_GROUP).equalTo("groupId","a");

        groupQuery.transientInclude("groupCreator");
        publicDatabase.query(groupQuery, new RecordQueryResponseHandler() {
            @Override
            public void onQuerySuccess(Record[] records) {
                if (records.length >0){
                    String groupId = (String) records[0].get("groupId");
//                    User user = (User) records[0].get("groupCreator");
                    Record groupCreator = (Record) records[0].getTransient().get("groupCreator");
                    mTextView.setText(groupCreator.getData().toString());
                }else {

                    mTextView.setText(String.valueOf(records.length));
                }
            }

            @Override
            public void onQueryError(Error error) {
                mTextView.setText(error.toString());
            }
        });


    }

    public void addDummyData(View view) {
        Record userRecord = new Record(TABLE_NAME_USER);
        userRecord.set("userId", "1");
        userRecord.set("userEmail", "one@one.com");

        mSkygear.getPublicDatabase().save(userRecord, new RecordSaveResponseHandler() {
            @Override
            public void onSaveSuccess(Record[] records) {
                mTextView.setText(records.toString());
            }

            @Override
            public void onPartiallySaveSuccess(Map<String, Record> successRecords, Map<String, Error> errors) {
                mTextView.setText(errors.toString());
            }

            @Override
            public void onSaveFail(Error error) {
                mTextView.setText(error.toString());
            }
        });

        Reference userReference = new Reference(userRecord);
        Record groupRecord = new Record(TABLE_NAME_GROUP);
        groupRecord.set("groupId", "a");
        groupRecord.set("groupCreator", userReference);

        mSkygear.getPublicDatabase().save(groupRecord, new RecordSaveResponseHandler() {
            @Override
            public void onSaveSuccess(Record[] records) {
                mTextView.setText("Record Saved Successfully");
//                mTextView.setText(records.toString());

            }

            @Override
            public void onPartiallySaveSuccess(Map<String, Record> successRecords, Map<String, Error> errors) {
                mTextView.setText(errors.toString());
            }

            @Override
            public void onSaveFail(Error error) {
                mTextView.setText(error.toString());
            }
        });
    }


    class User {

        private String userId;
        private String userEmail;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
    }

    class Group {

        private String groupId;
        private String groupName;
        private User groupCreater;
        private Set<User> groupMembers;

        public Set<User> getGroupMembers() {
            return groupMembers;
        }

        public void setGroupMembers(Set<User> groupMembers) {
            this.groupMembers = groupMembers;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public User getGroupCreater() {
            return groupCreater;
        }

        public void setGroupCreater(User groupCreater) {
            this.groupCreater = groupCreater;
        }

    }
}
