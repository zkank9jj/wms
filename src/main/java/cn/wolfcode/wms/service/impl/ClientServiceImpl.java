package cn.wolfcode.wms.service.impl;

import cn.wolfcode.wms.domain.Client;
import cn.wolfcode.wms.mapper.ClientMapper;
import cn.wolfcode.wms.query.QueryObject;
import cn.wolfcode.wms.service.IClientService;
import cn.wolfcode.wms.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements IClientService {
    @Autowired
    private ClientMapper clientMapper;

    public void saveOrUpdate(Client entity) {
        if (!Optional.ofNullable(entity.getId()).isPresent()) {
            clientMapper.insert(entity);//没有ID
        } else {
            clientMapper.updateByPrimaryKey(entity);
        }
    }

    public void delete(Long id) {
        clientMapper.deleteByPrimaryKey(id);
    }

    public Client get(Long id) {
        return clientMapper.selectByPrimaryKey(id);
    }

    public List<Client> list() {
        return clientMapper.selectAll();
    }

    public PageResult query(QueryObject qo) {
        Integer rows = clientMapper.selectForCount(qo);
        if (rows == 0) {
            return PageResult.EMPTY_PAGE;
        }
        List<?> data = clientMapper.selectForList(qo);
        return new PageResult(qo.getCurrentPage(), qo.getPageSize(), data, rows);
    }
}
