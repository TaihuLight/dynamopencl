package fr.dynamo.performance;

import java.util.concurrent.atomic.AtomicInteger;

public class Progress {

  private String jobId;
  private AtomicInteger total = new AtomicInteger(0);
  private AtomicInteger finished = new AtomicInteger(0);;

  public Progress(String jobId) {
    super();
    this.jobId = jobId;
  }

  public int incrementTotal(){
    return total.incrementAndGet();
  }

  public int incrementFinished(){
    return finished.incrementAndGet();
  }

  public float getProgress(){
    if(total.get() == 0) return 0;

    return (float)finished.get() / total.get();
  }

  public String getJobId() {
    return jobId;
  }
  public int getTotal() {
    return total.get();
  }
  public int getFinished() {
    return finished.get();
  }
}
