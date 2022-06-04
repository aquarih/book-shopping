package myssm.ioc;

import myssm.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {
    private Map<String, Object> beanMap = new HashMap<>();
    private String path = "applicationContext.xml";

    public ClassPathXmlApplicationContext(){
        this("applicationContext.xml");
    }

    public ClassPathXmlApplicationContext(String path){
        if(StringUtil.isEmpty(path)){
            throw new RuntimeException("IOC容器的配置文件沒有指定");
        }

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            //1. 創建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2. 創建DocumentBuilder對象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3. 創建Document對象
            Document document = documentBuilder.parse(inputStream);

            //4. 獲取所有的bean節點
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for(int i = 0;i < beanNodeList.getLength();i++){
                Node beanNode = beanNodeList.item(i);
                if(beanNode.getNodeType() == Node.ELEMENT_NODE){
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class beanClass = Class.forName(className);
                    //創建bean對象
                    Object beanObj = beanClass.newInstance();
                    //將bean對象保存到map容器中
                    beanMap.put(beanId, beanObj);
                }
            }

            //5. 組裝bean之間的依賴關係
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if(beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    NodeList beanChildNodeList = beanElement.getChildNodes();
                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        Node beanChildNode = beanChildNodeList.item(j);
                        if(beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())){
                            Element propertyElement = (Element) beanChildNode;
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");
                            //1) 找到propertyRef對應的對象
                            Object refObj = beanMap.get(propertyRef);
                            //2) 將refObj設置到當前bean對應的對象的property屬性上去
                            Object beanObj = beanMap.get(beanId);
                            Class<?> beanClazz = beanObj.getClass();
                            Field propertyField = beanClazz.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            propertyField.set(beanObj, refObj);
                        }
                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
