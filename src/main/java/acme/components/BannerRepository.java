
package acme.components;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banner.Banner;
import acme.framework.helpers.MessageHelper;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from Banner b where b.startDisplay < CURRENT_TIMESTAMP and b.endDisplay > CURRENT_TIMESTAMP")
	int countBanners();

	default Banner randomiseBanner() {
		int bannerCount;
		int bannerIndex;
		Banner defaultBanner;
		PageRequest pageRequest;
		String defaultSlogan;
		Banner result;
		defaultBanner = new Banner();
		defaultSlogan = MessageHelper.getMessage("master.banner.alt");
		defaultBanner.setSlogan(defaultSlogan);
		defaultBanner.setImgLink("images/banner.png");
		defaultBanner.setDocLink("https://www.us.es/");
		bannerCount = this.countBanners();
		if (bannerCount == 0)
			return defaultBanner;
		bannerIndex = ThreadLocalRandom.current().nextInt(bannerCount);
		pageRequest = PageRequest.of(bannerIndex, 1);
		result = this.findAllBanners(pageRequest).stream().findFirst().orElse(defaultBanner);
		return result;
	}

	@Query("select b from Banner b where b.startDisplay < CURRENT_TIMESTAMP and b.endDisplay > CURRENT_TIMESTAMP")
	List<Banner> findAllBanners(PageRequest pageRequest);

}
