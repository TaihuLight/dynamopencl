package fr.dynamo.test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.amd.aparapi.Range;
import com.amd.aparapi.device.Device.TYPE;
import com.amd.aparapi.device.OpenCLDevice;
import com.amd.aparapi.internal.opencl.OpenCLPlatform;

import fr.dynamo.DevicePreference;
import fr.dynamo.execution.DeviceQueue;
import fr.dynamo.threading.TileKernel;

public class DeviceQueueTest {

  private DeviceQueue deviceQueue = new DeviceQueue();

  private TileKernel kernel = new TileKernel(Range.create(0)) {

    @Override
    public void run() {
    }
  };

  @Before
  public void prepare(){
    OpenCLPlatform platform = new OpenCLPlatform();

    OpenCLDevice device = new OpenCLDevice(platform, 0, TYPE.CPU);
    deviceQueue.add(device);

    device = new OpenCLDevice(platform, 1, TYPE.CPU);
    deviceQueue.add(device);

    device = new OpenCLDevice(platform, 2, TYPE.GPU);
    deviceQueue.add(device);

    device = new OpenCLDevice(platform, 3, TYPE.GPU);
    deviceQueue.add(device);
  }

  @Test
  public void testSizes() {
    assertEquals(4, deviceQueue.size());
    deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(3, deviceQueue.size());
    deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(2, deviceQueue.size());
    deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(1, deviceQueue.size());
    deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(0, deviceQueue.size());
    deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(0, deviceQueue.size());
  }

  @Test
  public void testAddition() {
    deviceQueue = new DeviceQueue();
    assertEquals(0, deviceQueue.size());
    deviceQueue.add(new OpenCLDevice(new OpenCLPlatform(), 0, TYPE.CPU));
    assertEquals(1, deviceQueue.size());
  }


  @Test
  public void testExclusivePreferences() {
    OpenCLDevice device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_ONLY);
    assertEquals(TYPE.CPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_ONLY);
    assertEquals(TYPE.GPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_ONLY);
    assertEquals(TYPE.CPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_ONLY);
    assertEquals(null, device);

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_ONLY);
    assertEquals(TYPE.GPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_ONLY);
    assertEquals(null, device);

  }

  @Test
  public void testCpuPreferences() {
    OpenCLDevice device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_PREFERRED);
    assertEquals(TYPE.CPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_PREFERRED);
    assertEquals(TYPE.CPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_PREFERRED);
    assertEquals(TYPE.GPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_PREFERRED);
    assertEquals(TYPE.GPU, device.getType());
  }

  @Test
  public void testGpuPreferences() {
    OpenCLDevice device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_PREFERRED);
    assertEquals(TYPE.GPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_PREFERRED);
    assertEquals(TYPE.GPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_PREFERRED);
    assertEquals(TYPE.CPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.GPU_PREFERRED);
    assertEquals(TYPE.CPU, device.getType());
  }

  @Test
  public void testVaguePreferences() {
    OpenCLDevice device = deviceQueue.findFittingDevice(kernel, DevicePreference.CPU_ONLY);

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(TYPE.GPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(TYPE.CPU, device.getType());

    device = deviceQueue.findFittingDevice(kernel, DevicePreference.NONE);
    assertEquals(TYPE.GPU, device.getType());
  }

}
