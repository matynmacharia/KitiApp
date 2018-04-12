package app.kiti.com.kitiapp.scheduler;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import app.kiti.com.kitiapp.firebase.SyncManager;

/**
 * Created by Ankit on 4/12/2018.
 */

public class ShowAdJobService extends JobService{

    public static final String TAG = ShowAdJobService.class.getSimpleName();
    SyncManager syncManager;
    @Override
    public void onCreate() {
        super.onCreate();
        syncManager = new SyncManager();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        // job is to update preference and send broadcast to enable navigation button or show timer
        Log.d(TAG,"jobStarted");
        syncManager.logJob();
        jobFinished(job,true);
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG,"jobStopped");
        return false; // Answers the question: "Should this job be retried?"
    }

}
