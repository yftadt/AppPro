package test.app.ui.web;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//字符串内提取图片地址
public class ImageUrlExtract {

    // 匹配图片URL的正则表达式（支持http/https开头，常见图片后缀）
    private static final String REGEX_IMAGE_URL = "https?://[^\\s]*?\\.(jpg|jpeg|png|gif|bmp|webp)";

    // 匹配base64图片的正则表达式（匹配data:image开头的base64数据）
    private static final String REGEX_BASE64_IMAGE = "data:image/[^;]+;base64,[^\\s\"]+";

    /**
     * 提取字符串中的所有图片URL
     *
     * @param content 待提取的字符串
     * @return 图片URL列表（无则返回空列表）
     */
    public static ArrayList<String> extractImageUrls(String content) {
        ArrayList<String> imageUrls = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            return imageUrls;
        }

        // 编译正则表达式
        Pattern pattern = Pattern.compile(REGEX_IMAGE_URL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        // 循环匹配所有符合条件的URL
        while (matcher.find()) {
            String imageUrl = matcher.group();
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    /**
     * 提取字符串中的所有base64格式图片
     *
     * @param content 待提取的字符串
     * @return base64图片数据列表（无则返回空列表）
     */
    public static ArrayList<String> extractBase64Images(String content) {
        ArrayList<String> base64Images = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            return base64Images;
        }

        Pattern pattern = Pattern.compile(REGEX_BASE64_IMAGE);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String base64Image = matcher.group();
            base64Images.add(base64Image);
        }
        return base64Images;
    }

    /**
     * 提取字符串中所有图片（URL + Base64）
     *
     * @param content 待提取的字符串
     * @return 所有图片相关内容的列表
     */
    public static ArrayList<String> extractAllImages(String content) {
        ArrayList<String> allImages = new ArrayList<>();
        allImages.addAll(extractImageUrls(content));
        allImages.addAll(extractBase64Images(content));
        return allImages;
    }

    // 测试示例
    public static void test() {
        String testContent = "这是一段测试文本，包含图片URL：https://example.com/1.jpg，还有https://test.png，" +
                "以及base64图片：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAAUVBMVEUAAAD/" +
                "///9fX18PDw8ZGRkYGBgZGRkZGRkYGBgYGBgZGRkYGBgYGBgYGBgAAAACnRSTlMAEBAQEBQQFBQUFd3d3f4+Pjg4ODj4+Pn5+fo6Ojp6enq7O3t7e3u7u7v7+/w8PDx8fHy8vLz8vP09PT19fX29vb39/f4+Pj5+fm6Ojo6Onp6enq6+tr7e3s7u7t7u7v7+/w8vDx8fHy8vLz8vP09PT19fX29vb39/f4+Pj5+fm6Ojo6Onp6enq6+tr7e3s7u7t7u7v7+/w==";

        // 提取图片URL
        ArrayList<String> urls = extractImageUrls(testContent);
        System.out.println("提取的图片URL：");
        for (String url : urls) {
            System.out.println(url);
        }

        // 提取base64图片
        ArrayList<String> base64s = extractBase64Images(testContent);
        System.out.println("\n提取的Base64图片：");
        for (String base64 : base64s) {
            System.out.println(base64.substring(0, 50) + "..."); // 只打印前50个字符，避免过长
        }

        // 提取所有图片
        ArrayList<String> all = extractAllImages(testContent);
        System.out.println("\n提取的所有图片数量：" + all.size());
    }
}