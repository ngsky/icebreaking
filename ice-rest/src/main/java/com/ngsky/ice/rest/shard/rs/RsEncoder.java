package com.ngsky.ice.rest.shard.rs;

import com.ngsky.ice.rest.exception.ForbiddenException;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * <dl>
 * <dt>RsEncoder</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 19-7-30 下午11:47</dd>
 * </dl>
 *
 * @author ngsky
 */
public class RsEncoder {

    private int dataShards;
    private int parityShards;

    public static final int BYTES_IN_INT = 4;

    public RsEncoder(int dataShards, int parityShards) {
        this.dataShards = dataShards;
        this.parityShards = parityShards;
    }

    public byte[][] enode(File file) throws ForbiddenException, IOException {
        if (null == file || !file.exists()) {
            return null;
        }
        final int fileSize = (int) file.length();
        final int storedSize = fileSize + BYTES_IN_INT;
        // 每个分片快数据大小
        final int shardSize = (storedSize + dataShards - 1) / dataShards;
        // 计算文件总大小
        final int bufferSize = shardSize * dataShards;
        final byte[] allBytes = new byte[bufferSize];
        // int 转 byte
        ByteBuffer.wrap(allBytes).putInt(fileSize);
        InputStream in = new FileInputStream(file);
        int bytesRead = in.read(allBytes, BYTES_IN_INT, fileSize);
        if (bytesRead != fileSize) {
            throw new ForbiddenException("not enough bytes read");
        }
        in.close();
        // 构建分片
        byte[][] shards = new byte[(dataShards + parityShards)][shardSize];
        // 填充数据分片
        for (int i = 0; i < dataShards; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }
        // 计算校验分片
        ReedSolomon reedSolomon = ReedSolomon.create(dataShards, parityShards);
        reedSolomon.encodeParity(shards, 0, shardSize);

        // Write out the resulting files.
//        for (int i = 0; i < dataShards + parityShards; i++) {
//            File outputFile = new File(
//                    file.getParentFile(),
//                    file.getName() + "." + i);
//            OutputStream out = new FileOutputStream(outputFile);
//            out.write(shards[i]);
//            out.close();
//            System.out.println("wrote " + outputFile);
//        }
        return shards;
    }
}
