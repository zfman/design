package myutil;

public class Result {
		private int type;
		private int totalLen;
		private byte[] data;
		public Result(int type, int totalLen, byte[] data) {
			super();
			this.type = type;
			this.totalLen = totalLen;
			this.data = data;
		}
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
