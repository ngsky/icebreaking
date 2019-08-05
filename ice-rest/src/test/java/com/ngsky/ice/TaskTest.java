package com.ngsky.ice;

import com.ngsky.ice.rest.BaseTest;
import com.ngsky.ice.rest.common.IceExecutorService;
import com.ngsky.ice.rest.common.Task;
import org.junit.Test;

/**
 * <dl>
 * <dt>TaskTest</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 19-7-25 下午10:28</dd>
 * </dl>
 *
 * @author ngsky
 */
public class TaskTest extends BaseTest {

    @Test
    public void testTask(){

        System.out.println("--------start");
        IceExecutorService executor = new IceExecutorService();
        executor.execute(new MyTask());
        System.out.println("--------end");
    }

    class MyTask extends Task {
        @Override
        protected void doWork() throws Exception {
            Thread.sleep(3000);
            System.out.println("----------");
        }

        @Override
        public void notifyResult(boolean isSuccess) {

        }
    }

}
