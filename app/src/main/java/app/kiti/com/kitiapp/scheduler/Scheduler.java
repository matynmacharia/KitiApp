package app.kiti.com.kitiapp.scheduler;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Ankit on 4/12/2018.
 */

public class Scheduler {

    public static final String TAG = Scheduler.class.getSimpleName();

    private final Context mContext;
    private FirebaseJobDispatcher dispatcher;

    public Scheduler(Context context){
        this.mContext = context;
        // Create a new dispatcher using the Google Play driver.
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mContext));
    }

    public void shchedule(String tag){

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(ShowAdJobService.class)
                // uniquely identifies the job
                .setTag(tag)
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 10))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                )
                .build();

        Log.d(TAG,"scheduling job");
        dispatcher.mustSchedule(myJob);

    }

    public void cancelJob(String tag){
        dispatcher.cancel(tag);
    }

    public void cancelAllJobs(){
        dispatcher.cancelAll();
    }

}
