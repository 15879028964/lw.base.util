import com.yy.application.util.DateExtensionUtil;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;


/**
 * @author David.liu
 * @description 测试时间相关
 * @date 2020/5/7 20:31
 */

public class DataTest {

    @Test
    public void dateExtensionUtilTest() throws InterruptedException {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();
        Thread.sleep(1000);
        String s = DateExtensionUtil.formatDuration(localDateTime);
        System.out.println("接口耗时"+s);
    }
}
