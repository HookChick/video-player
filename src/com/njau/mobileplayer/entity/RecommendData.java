package com.njau.mobileplayer.entity;

import java.util.List;

/**
 * 推荐界面请求的数据封装的Bean对象
 * @author zhangcan
 *
 */
public class RecommendData {

    private InfoEntity info;

    private List<ListEntity> list;

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class InfoEntity {
        private int count;
        private double np;

        public void setCount(int count) {
            this.count = count;
        }

        public void setNp(double np) {
            this.np = np;
        }

        public int getCount() {
            return count;
        }

        public double getNp() {
            return np;
        }
    }

    public static class ListEntity {
        private int status;
        private String comment;
        private String bookmark;
        private String text;
        private String up;
        private String share_url;
        private int down;
        private int forward;
        
        private UEntity u;
        private String passtime;

        private VideoEntity video;
        /**
         * 图片
         */
        private ImageEntity image;

        /**
         * gif图片
         */
        private GifEntity gif;
        
        private List<TopCommentsEntity> top_comments;
        
        private String type;
        private String id;

        
		public List<TopCommentsEntity> getTop_comments() {
			return top_comments;
		}

		public void setTop_comments(List<TopCommentsEntity> top_comments) {
			this.top_comments = top_comments;
		}

		public GifEntity getGif() {
            return gif;
        }

        public void setGif(GifEntity gif) {
            this.gif = gif;
        }

        public ImageEntity getImage() {
            return image;
        }

        public void setImage(ImageEntity image) {
            this.image = image;
        }

        private List<TagsEntity> tags;

        public void setStatus(int status) {
            this.status = status;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public void setU(UEntity u) {
            this.u = u;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public void setVideo(VideoEntity video) {
            this.video = video;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTags(List<TagsEntity> tags) {
            this.tags = tags;
        }

        public int getStatus() {
            return status;
        }

        public String getComment() {
            return comment;
        }

        public String getBookmark() {
            return bookmark;
        }

        public String getText() {
            return text;
        }

        public String getUp() {
            return up;
        }

        public String getShare_url() {
            return share_url;
        }

        public int getDown() {
            return down;
        }

        public int getForward() {
            return forward;
        }

        public UEntity getU() {
            return u;
        }

        public String getPasstime() {
            return passtime;
        }

        public VideoEntity getVideo() {
            return video;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public List<TagsEntity> getTags() {
            return tags;
        }

        public static class UEntity {
            private boolean is_v;
            private String uid;
            private boolean is_vip;
            private String name;
            private List<String> header;

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public String getUid() {
                return uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public String getName() {
                return name;
            }

            public List<String> getHeader() {
                return header;
            }
        }

        public static class TopCommentsEntity{
        	private int voicetime;
        	private int status;
        	private int hate_count;
        	private String cmt_type;
        	private int precid;
        	private String content;
        	private int like_count;
        	
        	private CommentUEntity u;
        	
        	private int preuid;
        	private String passtime;
        	private String voiceuri;
        	private int id;
        	
        	
        	public int getVoicetime() {
				return voicetime;
			}


			public void setVoicetime(int voicetime) {
				this.voicetime = voicetime;
			}


			public int getStatus() {
				return status;
			}


			public void setStatus(int status) {
				this.status = status;
			}


			public int getHate_count() {
				return hate_count;
			}


			public void setHate_count(int hate_count) {
				this.hate_count = hate_count;
			}


			public String getCmt_type() {
				return cmt_type;
			}


			public void setCmt_type(String cmt_type) {
				this.cmt_type = cmt_type;
			}


			public int getPrecid() {
				return precid;
			}


			public void setPrecid(int precid) {
				this.precid = precid;
			}


			public String getContent() {
				return content;
			}


			public void setContent(String content) {
				this.content = content;
			}


			public int getLike_count() {
				return like_count;
			}


			public void setLike_count(int like_count) {
				this.like_count = like_count;
			}


			public CommentUEntity getU() {
				return u;
			}


			public void setU(CommentUEntity u) {
				this.u = u;
			}


			public int getPreuid() {
				return preuid;
			}


			public void setPreuid(int preuid) {
				this.preuid = preuid;
			}


			public String getPasstime() {
				return passtime;
			}


			public void setPasstime(String passtime) {
				this.passtime = passtime;
			}


			public String getVoiceuri() {
				return voiceuri;
			}


			public void setVoiceuri(String voiceuri) {
				this.voiceuri = voiceuri;
			}


			public int getId() {
				return id;
			}


			public void setId(int id) {
				this.id = id;
			}


			public static class CommentUEntity{
        		private List<String> header;
        		private String uid;
        		private boolean is_vip;
        		private String room_url;
        		private String sex;
        		private String room_name;
        		private String room_role;
        		private String room_icon;
        		private String name;
				public List<String> getHeader() {
					return header;
				}
				public void setHeader(List<String> header) {
					this.header = header;
				}
				public String getUid() {
					return uid;
				}
				public void setUid(String uid) {
					this.uid = uid;
				}
				public boolean isIs_vip() {
					return is_vip;
				}
				public void setIs_vip(boolean is_vip) {
					this.is_vip = is_vip;
				}
				public String getRoom_url() {
					return room_url;
				}
				public void setRoom_url(String room_url) {
					this.room_url = room_url;
				}
				public String getSex() {
					return sex;
				}
				public void setSex(String sex) {
					this.sex = sex;
				}
				public String getRoom_name() {
					return room_name;
				}
				public void setRoom_name(String room_name) {
					this.room_name = room_name;
				}
				public String getRoom_role() {
					return room_role;
				}
				public void setRoom_role(String room_role) {
					this.room_role = room_role;
				}
				public String getRoom_icon() {
					return room_icon;
				}
				public void setRoom_icon(String room_icon) {
					this.room_icon = room_icon;
				}
				public String getName() {
					return name;
				}
				public void setName(String name) {
					this.name = name;
				}
        		
        	}
        	
        }
        
        public static class GifEntity{

            private int width;
            private int height;
            private List<String> images;
            private List<String> gif_thumbnail;
            private List<String> download_url;

            public void setWidth(int width) {
                this.width = width;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public List<String> getImages() {
                return images;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public List<String> getDownload_url() {
                return download_url;
            }
        }

        public static class ImageEntity {

            private int height;
            private int width;
            private List<String> medium;
            private List<String> big;
            private List<String> download_url;
            private List<String> small;

            public void setHeight(int height) {
                this.height = height;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public void setMedium(List<String> medium) {
                this.medium = medium;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public void setSmall(List<String> small) {
                this.small = small;
            }

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }

            public List<String> getMedium() {
                return medium;
            }

            public List<String> getBig() {
                return big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public List<String> getSmall() {
                return small;
            }
        }

        public static class VideoEntity {
            private int playfcount;
            private int height;
            private int width;
            private int duration;
            private int playcount;
            private List<String> video;
            private List<String> thumbnail;
            private List<String> download;

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public int getPlayfcount() {
                return playfcount;
            }

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }

            public int getDuration() {
                return duration;
            }

            public int getPlaycount() {
                return playcount;
            }

            public List<String> getVideo() {
                return video;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public List<String> getDownload() {
                return download;
            }
        }

        public static class TagsEntity {
            private int id;
            private String name;

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }
}

