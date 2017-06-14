一：jackson 一些常用的注解
1.类属性，值为null不转化为json
作用在类
@JsonInclude(JsonInclude.Include.NON_NULL)
2.json有非属于类属性的，字段时，剔除这个字段
作用在类
@JsonIgnoreProperties(ignoreUnknown = true)
3.方法返回的值不不转化为json
作用在方法
@JsonIgnore
二：Retrofit的一些使用
1.替换url使用@Url
@POST
Call<String> docId(@Url String url, @Body BaseReq req);