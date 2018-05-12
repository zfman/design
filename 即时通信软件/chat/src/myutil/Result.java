package myutil;

/**
 * 封装一个消息，亦是一次解析的结果
 */
public class Result {
		//消息类型
		private int type;
		
		//消息总长度
		private int totalLen;
		
		//消息内容
		private byte[] data;
		
		//以消息的三个部分构造一个消息实体
		public Result(int type, int totalLen, byte[] data) {
			super();
			this.type = type;
			this.totalLen = totalLen;
			this.data = data;
		}
		
		//以下是setter、getter方法
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getTotalLen() {
			return totalLen;
		}
		public void setTotalLen(int totalLen) {
			this.totalLen = totalLen;
		}
		public byte[] getData() {
			return data;
		}
		public void setData(byte[] data) {
			this.data = data;
		}
}
