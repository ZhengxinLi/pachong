package tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Vector;

public class pachong {
    private Vector<ware> wares;
    public pachong() throws Exception {
        //String url = "https://search.jd.com/Search?keyword=纸张&enc=utf-8&pvid=1801720a0dce403d908678338cb67179";//第二页商品
          String url = "https://search.jd.com/Search?keyword=纸张&enc=utf-8&pvid=1801720a0dce403d908678338cb67179";
        //网址分析
        /*keyword:关键词（京东搜索框输入的信息）
         * enc：编码方式（可改动:默认UTF-8）
         * psort=3 //搜索方式  默认按综合查询 不给psort值
         * page=分业（不考虑动态加载时按照基数分业，每一页30条，这里就不演示动态加载）
         * 注意：受京东商品个性化影响，准确率无法保障
         * */
        Document doc = Jsoup.connect(url).maxBodySize(0).get();
        //doc获取整个页面的所有数据
        Elements ulList = doc.select("ul[class='gl-warp clearfix']");
        Elements liList = ulList.select("li[class='gl-item']");
        //循环liList的数据
        wares=new Vector<>();
        for (Element item : liList) {
            //排除广告位置
            if (!item.select("span[class='p-promo-flag']").text().trim().equals("广告")) {
                //如果向存到数据库和文件里请自行更改
                String price=item.select("div[class='p-price']").select("i").text();//打印商品价格到控制台
                String name=item.select("div[class='p-name p-name-type-2']").select("em").text();//打印商品标题到控制台
                String shop=(item.select("div[class='p-shop']").select("span").select("a").text());//打印商品店名到控制台
                String img=(item.select("div[class='p-img']").select("a").select("img").attr("source-data-lazy-img"));//打印商品图像到控制台
                String location=item.select("div[class='p-img']").select("a").attr("href");
                ware ware=new ware();
                ware.setImg(img);
                ware.setPrice(Float.parseFloat(price));
                ware.setShopname(shop);
                ware.setLocation(location);
                String[] warename=name.split(" ");
                if(warename.length>=2) {
                    ware.setWarename(warename[0]);
                    ware.setOther(warename[1]);
                }
                else{
                    ware.setWarename("");
                    ware.setOther(warename[0]);
                }
                wares.add(ware);
            }
        }
    }

    public Vector<ware> getWares() {
        return wares;
    }

    public static void main(String []args){
        try {
            pachong pachong=new pachong();
            Vector<ware> wares=pachong.getWares();
            for(int i=0;i<wares.size();i++){
                System.out.println(wares.get(i).getWarename());
                System.out.println(wares.get(i).getImg());
                System.out.println(wares.get(i).getPrice());
                System.out.println(wares.get(i).getShopname());
                System.out.println(wares.get(i).getOther());
                System.out.println(wares.get(i).getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
