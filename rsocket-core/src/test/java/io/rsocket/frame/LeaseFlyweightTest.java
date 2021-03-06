package io.rsocket.frame;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LeaseFlyweightTest {

  @Test
  void leaseMetadata() {
    ByteBuf metadata = bytebuf("md");
    int ttl = 1;
    int numRequests = 42;
    ByteBuf lease = LeaseFlyweight.encode(ByteBufAllocator.DEFAULT, ttl, numRequests, metadata);

    Assertions.assertTrue(FrameHeaderFlyweight.hasMetadata(lease));
    Assertions.assertEquals(ttl, LeaseFlyweight.ttl(lease));
    Assertions.assertEquals(numRequests, LeaseFlyweight.numRequests(lease));
    Assertions.assertEquals(metadata, LeaseFlyweight.metadata(lease));
    lease.release();
  }

  @Test
  void leaseAbsentMetadata() {
    int ttl = 1;
    int numRequests = 42;
    ByteBuf lease = LeaseFlyweight.encode(ByteBufAllocator.DEFAULT, ttl, numRequests, null);

    Assertions.assertFalse(FrameHeaderFlyweight.hasMetadata(lease));
    Assertions.assertEquals(ttl, LeaseFlyweight.ttl(lease));
    Assertions.assertEquals(numRequests, LeaseFlyweight.numRequests(lease));
    Assertions.assertEquals(0, LeaseFlyweight.metadata(lease).readableBytes());
    lease.release();
  }

  private static ByteBuf bytebuf(String str) {
    return Unpooled.copiedBuffer(str, StandardCharsets.UTF_8);
  }
}
