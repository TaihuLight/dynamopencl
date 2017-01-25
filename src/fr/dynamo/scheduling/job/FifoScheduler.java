package fr.dynamo.scheduling.job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import fr.dynamo.threading.DynamoJob;
import fr.dynamo.threading.DynamoKernel;

public class FifoScheduler implements JobScheduler{

  private class JobComparator implements Comparator<DynamoJob>{

    @Override
    public int compare(DynamoJob o1, DynamoJob o2) {
      return o1.getSubmissionTime().compareTo(o2.getSubmissionTime());
    }

  }

  @Override
  public List<DynamoKernel> schedule(List<DynamoJob> jobs) {
    List<DynamoKernel> kernels =  new ArrayList<DynamoKernel>();

    PriorityQueue<DynamoJob> orderedJobs = new PriorityQueue<DynamoJob>(11, new JobComparator());

    for(DynamoJob job:orderedJobs){
      kernels.addAll(job.getKernelsToRun());
    }

    return kernels;
  }

}
