package com.birdstudio.mars.network.transport.netty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.birdstudio.eirene.utils.Assert;
import com.birdstudio.mars.network.buffer.ChannelBuffer;
import com.birdstudio.mars.network.buffer.ChannelBufferFactory;
import com.birdstudio.mars.network.buffer.ChannelBuffers;

/**
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 */
public class NettyBackedChannelBuffer implements ChannelBuffer {

	private final org.jboss.netty.buffer.ChannelBuffer buffer;

	public org.jboss.netty.buffer.ChannelBuffer nettyChannelBuffer() {
		return buffer;
	}

	public NettyBackedChannelBuffer(org.jboss.netty.buffer.ChannelBuffer buffer) {
		Assert.notNull(buffer, "buffer == null");
		this.buffer = buffer;
	}

	@Override
	public int capacity() {
		return buffer.capacity();
	}

	@Override
	public ChannelBuffer copy(int index, int length) {
		return new NettyBackedChannelBuffer(buffer.copy(index, length));
	}

	@Override
	public ChannelBufferFactory factory() {
		return NettyBackedChannelBufferFactory.getInstance();
	}

	@Override
	public byte getByte(int index) {
		return buffer.getByte(index);
	}

	@Override
	public void getBytes(int index, byte[] dst, int dstIndex, int length) {
		buffer.getBytes(index, dst, dstIndex, length);
	}

	@Override
	public void getBytes(int index, ByteBuffer dst) {
		buffer.getBytes(index, dst);
	}

	@Override
	public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {
		// careful
		byte[] data = new byte[length];
		buffer.getBytes(index, data, 0, length);
		dst.setBytes(dstIndex, data, 0, length);
	}

	@Override
	public void getBytes(int index, OutputStream dst, int length)
			throws IOException {
		buffer.getBytes(index, dst, length);
	}

	@Override
	public boolean isDirect() {
		return buffer.isDirect();
	}

	@Override
	public void setByte(int index, int value) {
		buffer.setByte(index, value);
	}

	@Override
	public void setBytes(int index, byte[] src, int srcIndex, int length) {
		buffer.setBytes(index, src, srcIndex, length);
	}

	@Override
	public void setBytes(int index, ByteBuffer src) {
		buffer.setBytes(index, src);
	}

	@Override
	public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
		// careful
		byte[] data = new byte[length];
		buffer.getBytes(srcIndex, data, 0, length);
		setBytes(0, data, index, length);
	}

	@Override
	public int setBytes(int index, InputStream src, int length)
			throws IOException {
		return buffer.setBytes(index, src, length);
	}

	@Override
	public ByteBuffer toByteBuffer(int index, int length) {
		return buffer.toByteBuffer(index, length);
	}

	@Override
	public byte[] array() {
		return buffer.array();
	}

	@Override
	public boolean hasArray() {
		return buffer.hasArray();
	}

	@Override
	public int arrayOffset() {
		return buffer.arrayOffset();
	}

	// AbstractChannelBuffer

	@Override
	public void clear() {
		buffer.clear();
	}

	@Override
	public ChannelBuffer copy() {
		return new NettyBackedChannelBuffer(buffer.copy());
	}

	@Override
	public void discardReadBytes() {
		buffer.discardReadBytes();
	}

	@Override
	public void ensureWritableBytes(int writableBytes) {
		buffer.ensureWritableBytes(writableBytes);
	}

	@Override
	public void getBytes(int index, byte[] dst) {
		buffer.getBytes(index, dst);
	}

	@Override
	public void getBytes(int index, ChannelBuffer dst) {
		// careful
		getBytes(index, dst, dst.writableBytes());
	}

	@Override
	public void getBytes(int index, ChannelBuffer dst, int length) {
		// careful
		if (length > dst.writableBytes()) {
			throw new IndexOutOfBoundsException();
		}
		getBytes(index, dst, dst.writerIndex(), length);
		dst.writerIndex(dst.writerIndex() + length);
	}

	@Override
	public void markReaderIndex() {
		buffer.markReaderIndex();
	}

	@Override
	public void markWriterIndex() {
		buffer.markWriterIndex();
	}

	@Override
	public boolean readable() {
		return buffer.readable();
	}

	@Override
	public int readableBytes() {
		return buffer.readableBytes();
	}

	@Override
	public byte readByte() {
		return buffer.readByte();
	}

	@Override
	public void readBytes(byte[] dst) {
		buffer.readBytes(dst);
	}

	@Override
	public void readBytes(byte[] dst, int dstIndex, int length) {
		buffer.readBytes(dst, dstIndex, length);
	}

	@Override
	public void readBytes(ByteBuffer dst) {
		buffer.readBytes(dst);
	}

	@Override
	public void readBytes(ChannelBuffer dst) {
		// careful
		readBytes(dst, dst.writableBytes());
	}

	@Override
	public void readBytes(ChannelBuffer dst, int length) {
		// carefule
		if (length > dst.writableBytes()) {
			throw new IndexOutOfBoundsException();
		}
		readBytes(dst, dst.writerIndex(), length);
		dst.writerIndex(dst.writerIndex() + length);
	}

	@Override
	public void readBytes(ChannelBuffer dst, int dstIndex, int length) {
		// careful
		if (readableBytes() < length) {
			throw new IndexOutOfBoundsException();
		}
		byte[] data = new byte[length];
		buffer.readBytes(data, 0, length);
		dst.setBytes(dstIndex, data, 0, length);
	}

	@Override
	public ChannelBuffer readBytes(int length) {
		return new NettyBackedChannelBuffer(buffer.readBytes(length));
	}

	@Override
	public void resetReaderIndex() {
		buffer.resetReaderIndex();
	}

	@Override
	public void resetWriterIndex() {
		buffer.resetWriterIndex();
	}

	@Override
	public int readerIndex() {
		return buffer.readerIndex();
	}

	@Override
	public void readerIndex(int readerIndex) {
		buffer.readerIndex(readerIndex);
	}

	@Override
	public void readBytes(OutputStream dst, int length) throws IOException {
		buffer.readBytes(dst, length);
	}

	@Override
	public void setBytes(int index, byte[] src) {
		buffer.setBytes(index, src);
	}

	@Override
	public void setBytes(int index, ChannelBuffer src) {
		// careful
		setBytes(index, src, src.readableBytes());
	}

	@Override
	public void setBytes(int index, ChannelBuffer src, int length) {
		// careful
		if (length > src.readableBytes()) {
			throw new IndexOutOfBoundsException();
		}
		setBytes(index, src, src.readerIndex(), length);
		src.readerIndex(src.readerIndex() + length);
	}

	@Override
	public void setIndex(int readerIndex, int writerIndex) {
		buffer.setIndex(readerIndex, writerIndex);
	}

	@Override
	public void skipBytes(int length) {
		buffer.skipBytes(length);
	}

	@Override
	public ByteBuffer toByteBuffer() {
		return buffer.toByteBuffer();
	}

	@Override
	public boolean writable() {
		return buffer.writable();
	}

	@Override
	public int writableBytes() {
		return buffer.writableBytes();
	}

	@Override
	public void writeByte(int value) {
		buffer.writeByte(value);
	}

	@Override
	public void writeBytes(byte[] src) {
		buffer.writeBytes(src);
	}

	@Override
	public void writeBytes(byte[] src, int index, int length) {
		buffer.writeBytes(src, index, length);
	}

	@Override
	public void writeBytes(ByteBuffer src) {
		buffer.writeBytes(src);
	}

	@Override
	public void writeBytes(ChannelBuffer src) {
		// careful
		writeBytes(src, src.readableBytes());
	}

	@Override
	public void writeBytes(ChannelBuffer src, int length) {
		// careful
		if (length > src.readableBytes()) {
			throw new IndexOutOfBoundsException();
		}
		writeBytes(src, src.readerIndex(), length);
		src.readerIndex(src.readerIndex() + length);
	}

	@Override
	public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
		// careful
		byte[] data = new byte[length];
		src.getBytes(srcIndex, data, 0, length);
		writeBytes(data, 0, length);
	}

	@Override
	public int writeBytes(InputStream src, int length) throws IOException {
		return buffer.writeBytes(src, length);
	}

	@Override
	public int writerIndex() {
		return buffer.writerIndex();
	}

	@Override
	public void writerIndex(int writerIndex) {
		buffer.writerIndex(writerIndex);
	}

	@Override
	public int compareTo(ChannelBuffer o) {
		return ChannelBuffers.compare(this, o);
	}
}