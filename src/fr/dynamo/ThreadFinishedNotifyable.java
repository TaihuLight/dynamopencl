package fr.dynamo;

import fr.dynamo.threading.DynamoThread;

public interface ThreadFinishedNotifyable {
  public void notifyListener(DynamoThread thread);
}
