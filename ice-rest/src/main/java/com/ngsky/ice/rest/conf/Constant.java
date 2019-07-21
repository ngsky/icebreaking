package com.ngsky.ice.rest.conf;

import com.ngsky.ice.comm.bean.CellDownResp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/7/11 18:11
 */
public class Constant {
    // 4MB = 4194304 byte
    public final static int CHUNK_SIZE = 4194304;

    public final static Map<String, LinkedBlockingQueue<CellDownResp.RespDown>> PACKET_MAP = new HashMap<>();
}
