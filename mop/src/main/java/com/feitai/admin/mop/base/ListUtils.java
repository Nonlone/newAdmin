package com.feitai.admin.mop.base;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListUtils {

    private ListUtils(){}
	
	public static List<Long> toLongList(String value, String split) {

		List<Long> list = new ArrayList<Long>();
		
        if (StringUtils.isBlank(value)) {
            return list;
        }
        
        String[] valueStrings = value.split(split);
        for (String valueString : valueStrings) {
        	list.add(Long.parseLong(valueString));
        }
        
        return list;
    }
	

    /**
     * 校验list是否为null或empty
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection<?> list) {

        if (null == list || list.isEmpty()) {
            return true;
        }

        return false;
    }


    public static boolean isAllEmpty(Collection<?>... list) {

        for (Collection<?> itemList : list) {
            if (isNotEmpty(itemList)) {
                return false;
            }
        }

        return true;
    }


    /**
     * 校验list是否不为null或empty
     * @param list
     * @return
     */
    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }
	
	@SuppressWarnings("rawtypes")
	public static boolean isEqualListValue(final Collection list1, final Collection list2) {
		
		if (list1 == list2) {
            return true;
        }
		
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        
        Iterator it1 = list1.iterator();
        Object obj1 = null;
        while (it1.hasNext()) {
            obj1 = it1.next();

            if (!list2.contains(obj1)) {
            	return false;
            }
            
        }
        
        return true;
	}
}
