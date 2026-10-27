package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Order;
import kr.ac.kopo.gnuyog.bookmarket.repository.OrderProRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderProService
{
    @Autowired
    private OrderProRepository orderProRepository;

    // 주문 저장 (Order를 저장하면 cascade로 고객·배송·주소·주문항목도 함께 저장됨)
    public void save(Order order)
    {
        orderProRepository.save(order);
    }

    // 주문 목록을 페이지 단위로 조회
    // pageNum: 보고 싶은 페이지 번호(1부터), sortField: 정렬 기준 필드 이름, sortDir: "asc" 또는 "desc"
    public Page<Order> listAll(int pageNum, String sortField, String sortDir)
    {
        int pageSize = 5;   // 한 페이지에 보여줄 주문 수

        Sort sort = Sort.by(sortField);   // 정렬 기준 필드 지정 (예: "orderId")

        if (sortDir.equals("asc"))
        {
            sort = sort.ascending();      // 오름차순 (작은 번호 → 큰 번호)
        }
        else
        {
            sort = sort.descending();     // 내림차순 (큰 번호 → 작은 번호)
        }

        // 페이지 번호는 0부터 시작하므로 화면의 1페이지 = 내부의 0페이지
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return orderProRepository.findAll(pageable);
    }

    // 주문 번호로 주문 1건 조회
    public Order get(Long id)
    {
        Optional<Order> result = orderProRepository.findById(id); // 결과를 상자째로 받음

        if (result.isPresent())        // 상자에 주문이 들어 있으면
        {
            return result.get();       // 꺼내서 반환
        }

        // 비어 있으면 무엇이 잘못되었는지 알 수 있는 메시지로 예외 발생
        throw new IllegalArgumentException("주문 번호 " + id + "에 해당하는 주문이 없습니다.");
    }

    // 주문 번호로 주문 1건 삭제 (cascade로 고객·배송·주소·주문항목도 함께 삭제됨)
    public void delete(Long id)
    {
        if (!orderProRepository.existsById(id))   // 그 번호의 주문이 DB에 없으면
        {
            throw new IllegalArgumentException("주문 번호 " + id + "에 해당하는 주문이 없으므로 삭제할 수 없습니다.");
        }

        orderProRepository.deleteById(id);        // 있으면 삭제
    }

    // 모든 주문 삭제 (각 주문마다 cascade가 적용되어 고객·배송·주소·주문항목도 모두 삭제됨)
    public void deleteAll()
    {
        orderProRepository.deleteAll();
    }
}