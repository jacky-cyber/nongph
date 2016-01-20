package cn.globalph.batch.core;

import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public abstract class SingletonJob extends Job {
}
