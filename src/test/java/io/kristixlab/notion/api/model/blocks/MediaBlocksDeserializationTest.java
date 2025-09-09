package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kristixlab.notion.api.model.common.RichText;
import org.junit.jupiter.api.Test;

public class MediaBlocksDeserializationTest extends BaseBlockDeserializationTest {

  @Test
  void testFile() {
    Block b = findBlockById("22dc5b96-8ec4-8005-b3e7-ef21374069cd");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("22dc5b96-8ec4-8005-b3e7-ef21374069cd", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-07-11T18:07:00.000Z", b.getCreatedTime());
    assertEquals("2025-07-11T18:07:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    assertEquals("file", b.getType());
    FileBlock block = b.asFile();
    assertNotNull(block.getFile());
    assertEquals(0, block.getFile().getCaption().size());
    assertEquals("file", block.getFile().getType());
    assertNotNull(block.getFile().getFile());
    assertEquals(
        "https://prod-files-secure.s3.us-west-2.amazonaws.com/b46317c6-e48b-4d5f-ae09-8edc20206ee1/f2088d6b-5b52-44de-905d-79c89ec22902/%D0%91%D0%B5%D0%B7%D1%8B%D0%BC%D1%8F%D0%BD%D0%BD%D1%8B%D0%B8-2025-07-09-1522.excalidraw?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=ASIAZI2LB4665P5HLRWM%2F20250712%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20250712T174125Z&X-Amz-Expires=3600&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEOn%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCXVzLXdlc3QtMiJHMEUCIQC5w1FSMudLufLlsBDCc3gS7Omy1IGB7pVQL07QcankOwIgTFSXBAqw6vny7FKLbNsv5AVqs0SgQDASwGC%2BmTbG21UqiAQI8v%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAAGgw2Mzc0MjMxODM4MDUiDIgVYDMWFMTQau9wUyrcA1Z9hRD0GIB3Z2MH%2B25N0LbPOYzrGJv9AhIHlngV91rj4JB%2FGmPAXT4ZL64hxEv4xQD7svDWNHk5O%2FW%2F3GcbVK33WgelZnDvPruZuCPZucK1U%2Fk96f5ayb6JvfdQNpoSk7I%2F5%2FvBFqJbZ4RVztbpNjeQ7N7dJ9%2FdIDpgWJGpNCS4BDD1pbWOey9o%2F4KiT4mOILFvxlGl%2FVGzVH22ulCp%2FWicamb%2FIX%2Fol%2FvYj8CAaYZEomQuwqM1eecrqABGfr0%2Bp%2B3Sv8buIgMa9A11N1%2BYVef7t5JJk3r8OOxY9OTnKi2IeixGQzae%2BQtOXbOnBqp%2F47rfMvI7gQI0apmhACEHUvwi02ZC%2F3OCWY9IieoW5JRgpcvDgdAx8BMo6YwmiEq8TWQTxA1ClCo1vHE0GwjdqfYzfElyKL7xO%2FZdFxUdeh%2Bdfy8u1aQsh0P%2FIjoRN8eTk2K3Vi8J7m58kKYWEksfgomnZRuKi09qOwqwSgNRL8tBJvfZfbxGib%2FDgDtsTo11BhRHhyzARGOkeZYaiZTYXA7iEqvmmt1hG06XATE4bke9jE5MpsiIiTWsXIkxvp2N%2BmWAH4IQWvIpebZYYA7FcgXfht7VEgh8rQF5lZXGFs8UdjUS%2BpunyBOH%2B0zOMIauysMGOqUB%2B7ITUyMsCdmmOc%2B3uuumJyM83YLjFkPsrC88gtFBBJUYoOkHS17o3Ve3%2BGWsjySnwiIad7qrg%2FlbZh%2BsiOoVw%2BuNEM%2FXmPWNwTKwG%2F4gBc0SdXk8aqmfk6TvfArxWQ4LC12pEY2KlK8UCZ1artpUd%2Fopt9WG9rFYFbvH4Phsox9E3uRElUEmWENamdS6Ee3%2FOLKSigHWrQ54tuLtkevV5ZFINPNC&X-Amz-Signature=c22aefaaed89bcae1a03de5c8c29396bc54d3fe7889787ae9128fe93740ae786&X-Amz-SignedHeaders=host&x-amz-checksum-mode=ENABLED&x-id=GetObject",
        block.getFile().getFile().getUrl());
    assertEquals("2025-07-12T18:41:25.020Z", block.getFile().getFile().getExpiryTime());
    assertEquals("Безымянный-2025-07-09-1522.excalidraw", block.getFile().getName());
  }

  @Test
  void testVideo() {
    Block b = findBlockById("22dc5b96-8ec4-80c6-a815-da7389e18bf3");
    assertNotNull(b);

    assertEquals("block", b.getObject());
    assertEquals("22dc5b96-8ec4-80c6-a815-da7389e18bf3", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-07-11T18:07:00.000Z", b.getCreatedTime());
    assertEquals("2025-07-11T18:07:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    assertEquals("video", b.getType());
    VideoBlock block = b.asVideo();
    assertNotNull(block.getVideo());
    assertEquals(0, block.getVideo().getCaption().size());
    assertEquals("file", block.getVideo().getType());
    assertNotNull(block.getVideo().getFile());
    assertEquals(
        "https://prod-files-secure.s3.us-west-2.amazonaws.com/b46317c6-e48b-4d5f-ae09-8edc20206ee1/9c70dd15-f992-485b-9a3b-05551743ac28/pixabay-172682-849651722.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=ASIAZI2LB4665P5HLRWM%2F20250712%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20250712T174125Z&X-Amz-Expires=3600&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEOn%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCXVzLXdlc3QtMiJHMEUCIQC5w1FSMudLufLlsBDCc3gS7Omy1IGB7pVQL07QcankOwIgTFSXBAqw6vny7FKLbNsv5AVqs0SgQDASwGC%2BmTbG21UqiAQI8v%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAAGgw2Mzc0MjMxODM4MDUiDIgVYDMWFMTQau9wUyrcA1Z9hRD0GIB3Z2MH%2B25N0LbPOYzrGJv9AhIHlngV91rj4JB%2FGmPAXT4ZL64hxEv4xQD7svDWNHk5O%2FW%2F3GcbVK33WgelZnDvPruZuCPZucK1U%2Fk96f5ayb6JvfdQNpoSk7I%2F5%2FvBFqJbZ4RVztbpNjeQ7N7dJ9%2FdIDpgWJGpNCS4BDD1pbWOey9o%2F4KiT4mOILFvxlGl%2FVGzVH22ulCp%2FWicamb%2FIX%2Fol%2FvYj8CAaYZEomQuwqM1eecrqABGfr0%2Bp%2B3Sv8buIgMa9A11N1%2BYVef7t5JJk3r8OOxY9OTnKi2IeixGQzae%2BQtOXbOnBqp%2F47rfMvI7gQI0apmhACEHUvwi02ZC%2F3OCWY9IieoW5JRgpcvDgdAx8BMo6YwmiEq8TWQTxA1ClCo1vHE0GwjdqfYzfElyKL7xO%2FZdFxUdeh%2Bdfy8u1aQsh0P%2FIjoRN8eTk2K3Vi8J7m58kKYWEksfgomnZRuKi09qOwqwSgNRL8tBJvfZfbxGib%2FDgDtsTo11BhRHhyzARGOkeZYaiZTYXA7iEqvmmt1hG06XATE4bke9jE5MpsiIiTWsXIkxvp2N%2BmWAH4IQWvIpebZYYA7FcgXfht7VEgh8rQF5lZXGFs8UdjUS%2BpunyBOH%2B0zOMIauysMGOqUB%2B7ITUyMsCdmmOc%2B3uuumJyM83YLjFkPsrC88gtFBBJUYoOkHS17o3Ve3%2BGWsjySnwiIad7qrg%2FlbZh%2BsiOoVw%2BuNEM%2FXmPWNwTKwG%2F4gBc0SdXk8aqmfk6TvfArxWQ4LC12pEY2KlK8UCZ1artpUd%2Fopt9WG9rFYFbvH4Phsox9E3uRElUEmWENamdS6Ee3%2FOLKSigHWrQ54tuLtkevV5ZFINPNC&X-Amz-Signature=c2f5725f1560ecb6f112e6cfab0237f1d95364c69731fe965a4811dff808adc1&X-Amz-SignedHeaders=host&x-amz-checksum-mode=ENABLED&x-id=GetObject",
        block.getVideo().getFile().getUrl());
    assertEquals("2025-07-12T18:41:25.016Z", block.getVideo().getFile().getExpiryTime());
  }

  @Test
  void testAudio() {
    Block b = findBlockById("22dc5b96-8ec4-804f-b113-e3a7d3ebe458");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("22dc5b96-8ec4-804f-b113-e3a7d3ebe458", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-07-11T18:06:00.000Z", b.getCreatedTime());
    assertEquals("2025-07-11T18:07:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("abaaea82-caae-ddsf-eads-888018852f04", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    assertEquals("audio", b.getType());
    AudioBlock block = b.asAudio();
    assertNotNull(block.getAudio());
    assertEquals(0, block.getAudio().getCaption().size());
    assertEquals("file", block.getAudio().getType());
    assertNotNull(block.getAudio().getFile());
    assertEquals(
        "https://prod-files-secure.s3.us-west-2.amazonaws.com/b46317c6-e48b-4d5f-ae09-8edc20206ee1/aa8b7f81-fe4f-43d8-bdba-ef374de3bbbb/mixkit-summer-fun-13.mp3?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=ASIAZI2LB4665P5HLRWM%2F20250712%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20250712T174125Z&X-Amz-Expires=3600&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEOn%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCXVzLXdlc3QtMiJHMEUCIQC5w1FSMudLufLlsBDCc3gS7Omy1IGB7pVQL07QcankOwIgTFSXBAqw6vny7FKLbNsv5AVqs0SgQDASwGC%2BmTbG21UqiAQI8v%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAAGgw2Mzc0MjMxODM4MDUiDIgVYDMWFMTQau9wUyrcA1Z9hRD0GIB3Z2MH%2B25N0LbPOYzrGJv9AhIHlngV91rj4JB%2FGmPAXT4ZL64hxEv4xQD7svDWNHk5O%2FW%2F3GcbVK33WgelZnDvPruZuCPZucK1U%2Fk96f5ayb6JvfdQNpoSk7I%2F5%2FvBFqJbZ4RVztbpNjeQ7N7dJ9%2FdIDpgWJGpNCS4BDD1pbWOey9o%2F4KiT4mOILFvxlGl%2FVGzVH22ulCp%2FWicamb%2FIX%2Fol%2FvYj8CAaYZEomQuwqM1eecrqABGfr0%2Bp%2B3Sv8buIgMa9A11N1%2BYVef7t5JJk3r8OOxY9OTnKi2IeixGQzae%2BQtOXbOnBqp%2F47rfMvI7gQI0apmhACEHUvwi02ZC%2F3OCWY9IieoW5JRgpcvDgdAx8BMo6YwmiEq8TWQTxA1ClCo1vHE0GwjdqfYzfElyKL7xO%2FZdFxUdeh%2Bdfy8u1aQsh0P%2FIjoRN8eTk2K3Vi8J7m58kKYWEksfgomnZRuKi09qOwqwSgNRL8tBJvfZfbxGib%2FDgDtsTo11BhRHhyzARGOkeZYaiZTYXA7iEqvmmt1hG06XATE4bke9jE5MpsiIiTWsXIkxvp2N%2BmWAH4IQWvIpebZYYA7FcgXfht7VEgh8rQF5lZXGFs8UdjUS%2BpunyBOH%2B0zOMIauysMGOqUB%2B7ITUyMsCdmmOc%2B3uuumJyM83YLjFkPsrC88gtFBBJUYoOkHS17o3Ve3%2BGWsjySnwiIad7qrg%2FlbZh%2BsiOoVw%2BuNEM%2FXmPWNwTKwG%2F4gBc0SdXk8aqmfk6TvfArxWQ4LC12pEY2KlK8UCZ1artpUd%2Fopt9WG9rFYFbvH4Phsox9E3uRElUEmWENamdS6Ee3%2FOLKSigHWrQ54tuLtkevV5ZFINPNC&X-Amz-Signature=b63d847ad1afc780e13652661d2f9a4d79017d9dd93fd80e8a2f4e718830edc9&X-Amz-SignedHeaders=host&x-amz-checksum-mode=ENABLED&x-id=GetObject",
        block.getAudio().getFile().getUrl());
    assertEquals("2025-07-12T18:41:25.026Z", block.getAudio().getFile().getExpiryTime());
  }

  @Test
  void testImage() {
    boolean found = false;
    for (Block block : response.getResults()) {
      if ("22dc5b96-8ec4-805b-8f58-c15a5cc8fd80".equals(block.getId())) {
        found = true;
        ImageBlock img = block.asImage();
        assertEquals("block", img.getObject());
        assertEquals("image", img.getType());
        assertFalse(img.hasChildren());
        assertFalse(img.getArchived());
        assertFalse(img.getInTrash());
        assertParent(img, "226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8");
        assertEquals("2025-07-11T18:05:00.000Z", img.getCreatedTime());
        assertEquals("2025-07-11T18:06:00.000Z", img.getLastEditedTime());
        assertNotNull(img.getCreatedBy());
        assertEquals("user", img.getCreatedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", img.getCreatedBy().getId());
        assertNotNull(img.getLastEditedBy());
        assertEquals("user", img.getLastEditedBy().getObject());
        assertEquals("abaaea82-caae-ddsf-eads-888018852f04", img.getLastEditedBy().getId());
        assertNotNull(img.getImage());
        assertEquals("file", img.getImage().getType());
        assertNotNull(img.getImage().getFile());
        assertNotNull(img.getImage().getFile().getUrl());
        assertTrue(img.getImage().getFile().getUrl().contains("IMG_3555.png"));
        assertNotNull(img.getImage().getFile().getExpiryTime());
        assertNotNull(img.getImage().getCaption());
        assertEquals(0, img.getImage().getCaption().size());
      }
    }
    assertTrue(found);
  }

  @Test
  void testImageWithCaption() {
    Block b = findBlockById("245c5b96-8ec4-809d-846c-c1327e9f7a91");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("245c5b96-8ec4-809d-846c-c1327e9f7a91", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-08-04T15:32:00.000Z", b.getCreatedTime());
    assertEquals("2025-08-04T18:57:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    ImageBlock img = b.asImage();
    assertEquals("image", img.getType());
    assertNotNull(img.getImage());
    assertEquals("file", img.getImage().getType());
    assertNotNull(img.getImage().getFile());
    assertEquals(
        "https://prod-files-secure.s3.us-west-2.amazonaws.com/b46317c6-e48b-4d5f-ae09-8edc20206ee1/300f244d-ed30-4fce-a094-fa7d8943b945/IMG_7601.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=ASIAZI2LB4662A2YH3NM%2F20250804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20250804T185725Z&X-Amz-Expires=3600&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEBMaCXVzLXdlc3QtMiJHMEUCIELFko1wf0swhIyRkyzEd06rVfYPwXOvmUPhR4UqA1VXAiEA10RHQaWwmgpzTPELY2CG2C6%2FW2jMZzXlENwq6eHOFUEq%2FwMITBAAGgw2Mzc0MjMxODM4MDUiDLf32kRBxf8J0ymbqSrcA3GfBkpe8YH0VHEb1notm81Yr2g%2BtsZT%2BEYNl%2F%2F4w181EYmtq7LiuC2ne8sgZ6GtCLOE7u8HrvrKP5l9OyrCIvgKPc7Bb2pI23FKdBO7%2BgAbZg4qSj06onk1d8uXrWDc5vkBHiH618sfHBI7NSHNT%2BlQVYTYJvDRm2iP5w6wCizO%2BG9vM%2ByQUPS38Su5%2FjhSBrEVvw1yWdiZu4gFqxuaxGxGjdIFNzLVRoBVvtZ8ESfH1AqkUl3q%2F3j%2FJqlo6D1vBxGHVkz70f5S0u0Qq77sYag1sCg%2ByUnCOUbzeZ2hcUf67YK7is8U9eAocqOie0Y6rk2pSTb5vcGQnj5Df9ajFfnKtK5ZwLc4XZwYiM5Dn1fhVtkL2cfv96hwXlCiXc30MbX2RFXszgUomiJBd%2B5scX06PvMeR6JpyV9pGiiAyC%2BIcVJcryOkEo6vMaTgDM1L0NwoypAfotSQPzzBwi2u6fEq%2Ff512BS1q3JYEAfR3s4yr0iu0Lh%2BFjig0S%2Bv2PN0RY480XmVVzO2gCRTGH0yZ4MbeImlgaGOUCIqrqb%2BX769M3FbzUT8cK3RE88RQpiF1HxL1v73TcN0wro9SjO2sbeuJX4JUKcdd%2FxAPJrLccWEk96l1BYNpe08I%2FYLMLL5w8QGOqUB6ncz%2BTtRtRicYB%2FDz5IzgCUga4DR%2Fbqa3OsVTs9So41HpnvagKWjS1t%2B18wO%2F7KXWB925jxNqMldu9ozWXA%2F61FWHqQggvhae11jYE0z1yXqnfsVOPV0DltgAb1ejR4%2BgGXRnf6vrbnqxZOKncVZGX0sSKDCiAg5rx0Cx%2BS6AyDuPP%2BZUXWwDm%2FId9o8LTaQxjnNWujiVWUtPnuM3BZc4myIZ4Fr&X-Amz-Signature=ff23f4a212d1817203615fea8ecba2e1e3fc654cd68880744db30aa134cb95bc&X-Amz-SignedHeaders=host&x-amz-checksum-mode=ENABLED&x-id=GetObject",
        img.getImage().getFile().getUrl());
    assertEquals("2025-08-04T19:57:25.352Z", img.getImage().getFile().getExpiryTime());

    assertNotNull(img.getImage().getCaption());
    assertEquals(2, img.getImage().getCaption().size());
    RichText caption1 = img.getImage().getCaption().get(0);
    assertEquals("text", caption1.getType());
    assertEquals("hello, ", caption1.getPlainText());
    assertFalse(caption1.getAnnotations().isUnderline());
    RichText caption2 = img.getImage().getCaption().get(1);
    assertEquals("text", caption2.getType());
    assertEquals("it’s me", caption2.getPlainText());
    assertTrue(caption2.getAnnotations().isUnderline());
  }

  @Test
  void testPdf() {
    Block b = findBlockById("245c5b96-8ec4-80bc-8ccf-f4e66c90faa0");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("245c5b96-8ec4-80bc-8ccf-f4e66c90faa0", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-08-04T15:31:00.000Z", b.getCreatedTime());
    assertEquals("2025-08-04T15:31:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    assertEquals("pdf", b.getType());
    PdfBlock block = b.asPdf();
    assertNotNull(block.getPdf());
    assertEquals(0, block.getPdf().getCaption().size());
    assertEquals("file", block.getPdf().getType());
    assertNotNull(block.getPdf().getFile());
    assertEquals(
        "https://prod-files-secure.s3.us-west-2.amazonaws.com/b46317c6-e48b-4d5f-ae09-8edc20206ee1/6561334b-fb9c-4da6-bdb7-4fc1592aa8a0/_agrumentacja_28.07.pdf?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=ASIAZI2LB4662A2YH3NM%2F20250804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20250804T185725Z&X-Amz-Expires=3600&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEBMaCXVzLXdlc3QtMiJHMEUCIELFko1wf0swhIyRkyzEd06rVfYPwXOvmUPhR4UqA1VXAiEA10RHQaWwmgpzTPELY2CG2C6%2FW2jMZzXlENwq6eHOFUEq%2FwMITBAAGgw2Mzc0MjMxODM4MDUiDLf32kRBxf8J0ymbqSrcA3GfBkpe8YH0VHEb1notm81Yr2g%2BtsZT%2BEYNl%2F%2F4w181EYmtq7LiuC2ne8sgZ6GtCLOE7u8HrvrKP5l9OyrCIvgKPc7Bb2pI23FKdBO7%2BgAbZg4qSj06onk1d8uXrWDc5vkBHiH618sfHBI7NSHNT%2BlQVYTYJvDRm2iP5w6wCizO%2BG9vM%2ByQUPS38Su5%2FjhSBrEVvw1yWdiZu4gFqxuaxGxGjdIFNzLVRoBVvtZ8ESfH1AqkUl3q%2F3j%2FJqlo6D1vBxGHVkz70f5S0u0Qq77sYag1sCg%2ByUnCOUbzeZ2hcUf67YK7is8U9eAocqOie0Y6rk2pSTb5vcGQnj5Df9ajFfnKtK5ZwLc4XZwYiM5Dn1fhVtkL2cfv96hwXlCiXc30MbX2RFXszgUomiJBd%2B5scX06PvMeR6JpyV9pGiiAyC%2BIcVJcryOkEo6vMaTgDM1L0NwoypAfotSQPzzBwi2u6fEq%2Ff512BS1q3JYEAfR3s4yr0iu0Lh%2BFjig0S%2Bv2PN0RY480XmVVzO2gCRTGH0yZ4MbeImlgaGOUCIqrqb%2BX769M3FbzUT8cK3RE88RQpiF1HxL1v73TcN0wro9SjO2sbeuJX4JUKcdd%2FxAPJrLccWEk96l1BYNpe08I%2FYLMLL5w8QGOqUB6ncz%2BTtRtRicYB%2FDz5IzgCUga4DR%2Fbqa3OsVTs9So41HpnvagKWjS1t%2B18wO%2F7KXWB925jxNqMldu9ozWXA%2F61FWHqQggvhae11jYE0z1yXqnfsVOPV0DltgAb1ejR4%2BgGXRnf6vrbnqxZOKncVZGX0sSKDCiAg5rx0Cx%2BS6AyDuPP%2BZUXWwDm%2FId9o8LTaQxjnNWujiVWUtPnuM3BZc4myIZ4Fr&X-Amz-Signature=cd349ab73c147f6de20a8303c8072e78b7106fb26bf4acef120dfb1f054bc62b&X-Amz-SignedHeaders=host&x-amz-checksum-mode=ENABLED&x-id=GetObject",
        block.getPdf().getFile().getUrl());
    assertEquals("2025-08-04T19:57:25.335Z", block.getPdf().getFile().getExpiryTime());
  }

  @Test
  void testEmbed() {
    Block b = findBlockById("245c5b96-8ec4-80df-9f2a-c6fc1e3064ed");
    assertNotNull(b);
    assertEquals("block", b.getObject());
    assertEquals("245c5b96-8ec4-80df-9f2a-c6fc1e3064ed", b.getId());
    assertEquals("page_id", b.getParent().getType());
    assertEquals("226c5b96-8ec4-801f-ad8e-cd6c19d8e0a8", b.getParent().getPageId());
    assertEquals("2025-08-04T19:04:00.000Z", b.getCreatedTime());
    assertEquals("2025-08-04T19:04:00.000Z", b.getLastEditedTime());
    assertEquals("user", b.getCreatedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getCreatedBy().getId());
    assertEquals("user", b.getLastEditedBy().getObject());
    assertEquals("aaaaaaa2-cccc-eeee-9999-222222222222", b.getLastEditedBy().getId());
    assertFalse(b.hasChildren());
    assertFalse(b.getArchived());
    assertFalse(b.getInTrash());

    EmbedBlock embed = b.asEmbed();
    assertEquals("embed", embed.getType());
    assertNotNull(embed.getEmbed());
    assertEquals("https://maps.app.goo.gl/Sr6odhLUhAEYaH4W7", embed.getEmbed().getUrl());
    assertNotNull(embed.getEmbed().getCaption());
    assertEquals(1, embed.getEmbed().getCaption().size());
    RichText caption = embed.getEmbed().getCaption().get(0);
    assertEquals("text", caption.getType());
    assertEquals("stress relief idea", caption.getPlainText());
    assertEquals("stress relief idea", caption.getText().getContent());
    assertNull(caption.getMention());
    assertNull(caption.getHref());
    assertNull(caption.getText().getLink());
  }
}
