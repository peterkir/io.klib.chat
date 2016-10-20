package io.klib.zookeeper.provider;
import java.io.File;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(name = "osgi.enroute.examples.zookeeper")
public class ZookeeperImpl extends ZooKeeperServerMain {
	
	private Thread thread;
	private ServerConfig config;

	@Activate
	void activate(BundleContext context) {
		File dir = context.getDataFile("zookeeper");
		config = new ServerConfig();
		config.parse(new String[] { "6789", dir.getAbsolutePath() });
		thread = new Thread(this::zk, "osgi.enroute.examples.zookeeper");
		thread.start();
	}

	@Deactivate
	void deactivate() {
		shutdown();
		thread.interrupt();
	}
	
	public void zk() {
		try {
			runFromConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ZK Exiting");
	}
}