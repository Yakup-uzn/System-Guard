package com.spam.mail.service;

import com.spam.mail.dto.SolvedRequest;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolvedFormSubmitter {

	public boolean submitForm(SolvedRequest request) {
	    ChromeOptions options = new ChromeOptions();
	    options.addArguments("--ignore-certificate-errors");
	    options.addArguments("--allow-insecure-localhost");
	    options.addArguments("--headless=new");
	    options.addArguments("--disable-dev-shm-usage");
	    options.addArguments("--no-sandbox");
	    options.addArguments("--window-size=1920,1080");

	    WebDriver driver = new ChromeDriver(options);

	    try {
	        driver.get(request.getFormLink());
	        System.out.println("🌍 Sayfa açıldı: " + request.getFormLink());

	        boolean isExpired = driver.findElements(By.cssSelector(".form-state.expired-form")).size() > 0;
	        if (isExpired) {
	            System.out.println("⚠️ Bu form daha önce çözümlenmiş olabilir. (expired-form bulundu)");
	            return false;
	        }

	        clickRadioByQuestionAndTitle(driver, "0", request.getCaseResult());
	        clickRadioByQuestionAndTitle(driver, "1", request.getEvaluationResult());
	        clickRadioByQuestionAndTitle(driver, "2", request.getName());

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement submitButton = wait.until(
	            ExpectedConditions.elementToBeClickable(By.cssSelector(".submit-form.ui.button"))
	        );
	        submitButton.click();

	        System.out.println("✅ Form başarıyla gönderildi.");
	        return true;

	    } catch (Exception e) {
	        System.out.println("❌ HATA:");
	        e.printStackTrace();
	        return false;
	    } finally {
	        driver.quit();
	    }
	}



    private void clickRadioByQuestionAndTitle(WebDriver driver, String questionId, String titleValue) {
        List<WebElement> container = driver.findElements(By.cssSelector(
            "div.question.singleSelect[id='" + questionId + "'] .ui.radio.checkbox"
        ));

        String normalizedTarget = normalize(titleValue);

        for (WebElement item : container) {
            try {
                WebElement label = item.findElement(By.tagName("label"));
                WebElement input = item.findElement(By.cssSelector("input[type='radio']"));

                String inputTitle = normalize(input.getAttribute("title"));
                String labelTitle = normalize(label.getAttribute("title"));
                String labelText  = normalize(label.getText());

                if (inputTitle.equals(normalizedTarget) ||
                    labelTitle.equals(normalizedTarget) ||
                    labelText.equals(normalizedTarget)) {

                    System.out.println("🔘 Seçiliyor: [id=" + questionId + "] " + titleValue);
                    label.click(); // label'a tıklamak daha güvenli
                    return;
                }

            } catch (Exception e) {
                System.out.println("⚠️ Seçenek okunamadı: " + e.getMessage());
            }
        }

        System.out.println("❌ Seçenek bulunamadı: [id=" + questionId + "] " + titleValue);
    }

    private String normalize(String input) {
        if (input == null) return "";
        return input.trim().toLowerCase().replaceAll("\\s+", ""); // boşluk ve büyük/küçük farkı yok
    }
}
